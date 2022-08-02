/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.randomvillagenames.events;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.TileEntityFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.randomvillagenames.util.RandomVillageNamesUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RandomVillageNamesSetVillageSignEvent {
	private static HashMap<Level, CopyOnWriteArrayList<BlockPos>> ignorevillages = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	private static HashMap<String, Integer> lastticks = new HashMap<String, Integer>();
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (!ignorevillages.containsKey(world)) {
			ignorevillages.put(world, new CopyOnWriteArrayList<BlockPos>());
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		String playername = player.getName().getString();
		
		int ticki = 0;
		if (lastticks.containsKey(playername)) {
			ticki = lastticks.get(playername);
		}
		
		if (ticki > 0) {
			lastticks.put(playername, ticki-1);
			return;
		}
		lastticks.put(playername, 200);
		
		ServerLevel serverworld = (ServerLevel)world;
		BlockPos ppos = player.blockPosition();
		
		BlockPos villagepos = BlockPosFunctions.getNearbyStructure(serverworld, StructureFeature.VILLAGE, ppos, 100);
		if (villagepos == null) {
			return;
		}
		
		if (ignorevillages.get(world).contains(villagepos)) {
			return;
		}
		
		BlockPos twonorth = villagepos.immutable().north(2);
		BlockPos signpos = BlockPosFunctions.getSurfaceBlockPos(serverworld, twonorth.getX(), twonorth.getZ());
		
		BlockState state = world.getBlockState(signpos);
		Block block = state.getBlock();
		while (!RandomVillageNamesUtil.isOverwritableBlockOrSign(block)) {
			signpos = signpos.above().immutable();
			if (signpos.getY() >= 256) {
				ignorevillages.get(world).add(villagepos);
				return;
			}
			
			state = world.getBlockState(signpos);
			block = state.getBlock();
		}
		
		if (RandomVillageNamesUtil.isSign(block)) {
			ignorevillages.get(world).add(villagepos);
			return;
		}
		
		try {
			Block northblock = world.getBlockState(signpos.north()).getBlock();
			if (!RandomVillageNamesUtil.isOverwritableBlockOrSign(northblock)) {
				world.setBlockAndUpdate(signpos, Blocks.OAK_WALL_SIGN.defaultBlockState().setValue(WallSignBlock.FACING, Direction.SOUTH));
			}
			else {
				world.setBlockAndUpdate(signpos, Blocks.OAK_SIGN.defaultBlockState());
			}
		}
		catch (ConcurrentModificationException ex) { }
		
		BlockEntity te = world.getBlockEntity(signpos);
		if (te instanceof SignBlockEntity == false) {
			return;
		}
		
		SignBlockEntity signentity = (SignBlockEntity)te;
		signentity.setMessage(0, new TextComponent("[Area] 60"));
		TileEntityFunctions.updateTileEntity(serverworld, signpos, signentity);
	}
	
	@SubscribeEvent
	public void onSignBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockState state = e.getState();
		if (RandomVillageNamesUtil.isSign(state.getBlock())) {
			BlockPos signpos = e.getPos();
			for (BlockPos villagepos : ignorevillages.get(world)) {
				if (BlockPosFunctions.withinDistance(villagepos, signpos, 10)) {
					ignorevillages.get(world).remove(villagepos);
					break;
				}
			}
		}
	}
}
