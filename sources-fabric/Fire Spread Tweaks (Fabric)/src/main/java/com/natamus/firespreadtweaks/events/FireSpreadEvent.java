/*
 * This is the latest source code of Fire Spread Tweaks.
 * Minecraft version: 1.19.3, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.firespreadtweaks.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective_fabric.functions.BlockFunctions;
import com.natamus.firespreadtweaks.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FireSpreadEvent {
	private static HashMap<BlockPos, Integer> ticksleft = new HashMap<BlockPos, Integer>();
	private static HashMap<Level, CopyOnWriteArrayList<BlockPos>> firepositions = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	
	private static List<Block> fireblocks = new ArrayList<Block>(Arrays.asList(Blocks.NETHERRACK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL));
	
	public static void onWorldTick(ServerLevel world) {
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
	
	public static void onWorldLoad(ServerLevel world) {
		BooleanValue firetickvalue = world.getGameRules().getRule(GameRules.RULE_DOFIRETICK);
		if (firetickvalue.get()) {
			firetickvalue.set(false, world.getServer());
		}
		
		firepositions.put(world, new CopyOnWriteArrayList<BlockPos>());
	}
	
	public static void onWorldUnload(ServerLevel world) {
		for (BlockPos firepos : firepositions.get(world)) {
			BlockState firestate = world.getBlockState(firepos);
			Block fireblock = firestate.getBlock();
			if (fireblock instanceof FireBlock) {
				world.setBlockAndUpdate(firepos, Blocks.AIR.defaultBlockState());
			}		
		}
	}
	
	public static void onNeighbourNotice(Level world, BlockPos pos, BlockState state, EnumSet<Direction> notifiedSides, boolean forceRedstoneUpdate) {
		if (world.isClientSide) {
			return;
		}
		
		Block block = state.getBlock();
		if (block instanceof FireBlock == false) {
			return;
		}
		
		Block belowblock = world.getBlockState(pos.below()).getBlock();
		if (BlockFunctions.isOneOfBlocks(fireblocks, belowblock)) {
			return;
		}
		
		ticksleft.put(pos, Util.getFireBurnDurationInTicks());
		firepositions.get(world).add(pos);
	}
}
