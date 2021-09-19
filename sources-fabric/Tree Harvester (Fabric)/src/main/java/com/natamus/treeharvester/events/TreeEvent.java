/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.17.x, mod version: 3.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Tree Harvester ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.treeharvester.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective_fabric.functions.BlockFunctions;
import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.CompareBlockFunctions;
import com.natamus.collective_fabric.functions.ToolFunctions;
import com.natamus.treeharvester.config.ConfigHandler;
import com.natamus.treeharvester.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TreeEvent {
	private static HashMap<Level, CopyOnWriteArrayList<List<BlockPos>>> processleaves = new HashMap<Level, CopyOnWriteArrayList<List<BlockPos>>>();
	
	public static void onWorldLoad(ServerLevel world) {
		processleaves.put(world, new CopyOnWriteArrayList<List<BlockPos>>());
	}
	
	public static void onWorldTick(ServerLevel world) { 
		if (processleaves.get(world).size() == 0) {
			return;
		}
		
		for (List<BlockPos> leaves : processleaves.get(world)) {
			BlockPos lasttr = null;
			int size = leaves.size();
			if (size > 0) {
				BlockPos tr = leaves.get(0);
				
				BlockFunctions.dropBlock(world, tr);
				
				leaves.remove(0);
				if (leaves.size() > 0) {
					tr = leaves.get(0);
					
					BlockFunctions.dropBlock(world, tr);
					
					leaves.remove(0);					
				}
				
				lasttr = tr.immutable();
			}
			
			if (leaves.size() == 0) {
				processleaves.get(world).remove(leaves);
				if (lasttr != null) {
					if (ConfigHandler.replaceSaplingIfBottomLogIsBroken.getValue()) {
						if (Util.lowerlogs.size() > 0) {
							BlockPos lowerlasttrpos = new BlockPos(lasttr.getX(), 1, lasttr.getZ());
							for (Pair<BlockPos, CopyOnWriteArrayList<BlockPos>> pair : Util.lowerlogs) {
								BlockPos breakpos = pair.getFirst();
								if (BlockPosFunctions.withinDistance(lowerlasttrpos, new BlockPos(breakpos.getX(), 1, breakpos.getZ()), 5)) {
									Util.replaceSapling(world, breakpos, pair.getSecond(), 1);
									Util.lowerlogs.remove(pair);
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean onTreeHarvest(Level world, Player player, BlockPos bpos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return true;
		}
		
		Block block = world.getBlockState(bpos).getBlock();
		if (!CompareBlockFunctions.isTreeLog(block)) {
			return true;
		}
		
		if (ConfigHandler.treeHarvestWithoutSneak.getValue()) {
			if (player.isShiftKeyDown()) {
				return true;
			}
		}
		else {
			if (!player.isShiftKeyDown()) {
				return true;
			}
		}
		
		ItemStack hand = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (ConfigHandler.mustHoldAxeForTreeHarvest.getValue()) {
			if (!ToolFunctions.isAxe(hand)) {
				return true;
			}
		}
		
		if (hand == null) {
			return true;
		}
		
		int logcount = Util.isTreeAndReturnLogAmount(world, bpos);
		if (logcount < 0) {
			return true;
		}
		
		Item handitem = hand.getItem();
		
		List<BlockPos> logstobreak = Util.getAllLogsToBreak(world, bpos, logcount);
		for (BlockPos logpos : logstobreak) {
			BlockState logstate = world.getBlockState(logpos);
			Block log = logstate.getBlock();
			
			world.destroyBlock(logpos, false);
			log.playerDestroy(world, player, logpos, logstate, null, hand);
			if (!player.isCreative()) {
				if (ConfigHandler.loseDurabilityPerHarvestedLog.getValue()) {
					handitem.mineBlock(hand, world, logstate, logpos, player);
				}
				if (ConfigHandler.increaseExhaustionPerHarvestedLog.getValue()) {
					player.causeFoodExhaustion(0.025F);
				}
			}
		}
		
		if (logstobreak.size() == 0) {
			return true;
		}
		
		if (ConfigHandler.enableFastLeafDecay.getValue() && !ConfigHandler.instantBreakLeavesAround.getValue()) {
			List<BlockPos> logs = new ArrayList<BlockPos>();
			List<BlockPos> leaves = new ArrayList<BlockPos>();
			
			Iterator<BlockPos> possiblelogs = BlockPos.betweenClosed(bpos.getX()-8, bpos.getY(), bpos.getZ()-8, bpos.getX()+8, Util.highestleaf.get(bpos), bpos.getZ()+8).iterator();
			while(possiblelogs.hasNext()) {
				BlockPos next = possiblelogs.next();
				if (CompareBlockFunctions.isTreeLog(world.getBlockState(next).getBlock())) {
					logs.add(next.immutable());
				}
			}
			
			Pair<Integer, Integer> hv = Util.getHorizontalAndVerticalValue(logcount);
			int h = hv.getFirst();
			
			Iterator<BlockPos> possibleleaves = BlockPos.betweenClosed(bpos.getX()-h, bpos.getY(), bpos.getZ()-h, bpos.getX()+h, Util.highestleaf.get(bpos), bpos.getZ()+h).iterator();
			while (possibleleaves.hasNext()) {
				BlockPos next = possibleleaves.next();
				if (CompareBlockFunctions.isTreeLeaf(world.getBlockState(next).getBlock(), ConfigHandler.enableNetherTrees.getValue())) {
					boolean logclose = false;
					for (BlockPos log : logs) {
						double distance = log.distSqr(next);
						if (distance < 7) {
							logclose = true;
							break;
						}
					}
					
					if (!logclose) {
						leaves.add(next.immutable());
					}
				}
			}
			
			Collections.shuffle(leaves);
			processleaves.get(world).add(leaves);
			Util.highestleaf.remove(bpos);
		}
		
		return false;
	}
}