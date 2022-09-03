/*
 * This is the latest source code of Fire Spread Tweaks.
 * Minecraft version: 1.19.2, mod version: 1.5.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.firespreadtweaks.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.firespreadtweaks.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FireSpreadEvent {
	private static HashMap<BlockPos, Integer> ticksleft = new HashMap<BlockPos, Integer>();
	private static HashMap<Level, CopyOnWriteArrayList<BlockPos>> firepositions = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	
	private static List<Block> fireblocks = new ArrayList<Block>(Arrays.asList(Blocks.NETHERRACK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL));
	
	@SubscribeEvent
	public void onWorldTick(LevelTickEvent e) {
		Level world = e.level;
		if (world.isClientSide || !e.phase.equals(Phase.END)) {
			return;
		}
		
		if (!firepositions.containsKey(world)) {
			firepositions.put(world, new CopyOnWriteArrayList<BlockPos>());
			return;
		}
		
		for (BlockPos firepos : firepositions.get(world)) {
			if (!ticksleft.containsKey(firepos)) {
				ticksleft.put(firepos, Util.getFireBurnDurationInTicks());
				continue;
			}
			
			int tl = ticksleft.get(firepos) -1;
			if (tl <= 0) {
				ticksleft.remove(firepos);
				firepositions.get(world).remove(firepos);
				
				BlockState firestate = world.getBlockState(firepos);
				Block fireblock = firestate.getBlock();
				if (fireblock instanceof FireBlock) {
					world.setBlockAndUpdate(firepos, Blocks.AIR.defaultBlockState());
				}
 				continue;
			}
			
			ticksleft.put(firepos, tl);
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(LevelEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		BooleanValue firetickvalue = world.getGameRules().getRule(GameRules.RULE_DOFIRETICK);
		if (firetickvalue.get()) {
			firetickvalue.set(false, world.getServer());
		}
		
		firepositions.put(world, new CopyOnWriteArrayList<BlockPos>());
	}
	
	@SubscribeEvent
	public void onWorldLoad(LevelEvent.Unload e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}		

		for (BlockPos firepos : firepositions.get(world)) {
			BlockState firestate = world.getBlockState(firepos);
			Block fireblock = firestate.getBlock();
			if (fireblock instanceof FireBlock) {
				world.setBlockAndUpdate(firepos, Blocks.AIR.defaultBlockState());
			}		
		}
	}
	
	@SubscribeEvent
	public void onNeighbourNotice(BlockEvent.NeighborNotifyEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		BlockState state = e.getState();
		Block block = state.getBlock();
		if (block instanceof FireBlock == false) {
			return;
		}
		
		BlockPos pos = e.getPos().immutable();
		Block belowblock = world.getBlockState(pos.below()).getBlock();
		if (BlockFunctions.isOneOfBlocks(fireblocks, belowblock)) {
			return;
		}
		
		ticksleft.put(pos, Util.getFireBurnDurationInTicks());
		firepositions.get(world).add(pos);
	}
}
