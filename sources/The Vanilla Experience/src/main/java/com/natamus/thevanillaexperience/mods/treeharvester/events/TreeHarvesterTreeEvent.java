/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.treeharvester.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.ToolFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.treeharvester.config.TreeHarvesterConfigHandler;
import com.natamus.thevanillaexperience.mods.treeharvester.util.TreeHarvesterUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class TreeHarvesterTreeEvent {
	private static HashMap<Level, CopyOnWriteArrayList<List<BlockPos>>> processleaves = new HashMap<Level, CopyOnWriteArrayList<List<BlockPos>>>();
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		processleaves.put(world, new CopyOnWriteArrayList<List<BlockPos>>());
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
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
					if (TreeHarvesterConfigHandler.GENERAL.replaceSaplingIfBottomLogIsBroken.get()) {
						if (TreeHarvesterUtil.lowerlogs.size() > 0) {
							BlockPos lowerlasttrpos = new BlockPos(lasttr.getX(), 1, lasttr.getZ());
							for (Pair<BlockPos, CopyOnWriteArrayList<BlockPos>> pair : TreeHarvesterUtil.lowerlogs) {
								BlockPos breakpos = pair.getFirst();
								if (BlockPosFunctions.withinDistance(lowerlasttrpos, new BlockPos(breakpos.getX(), 1, breakpos.getZ()), 5)) {
									TreeHarvesterUtil.replaceSapling(world, breakpos, pair.getSecond(), 1);
									TreeHarvesterUtil.lowerlogs.remove(pair);
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onTreeHarvest(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockPos bpos = e.getPos();
		Block block = world.getBlockState(bpos).getBlock();
		if (!TreeHarvesterUtil.isTreeLog(block)) {
			return;
		}
		
		Player player = e.getPlayer();
		if (TreeHarvesterConfigHandler.GENERAL.treeHarvestWithoutSneak.get()) {
			if (player.isShiftKeyDown()) {
				return;
			}
		}
		else {
			if (!player.isShiftKeyDown()) {
				return;
			}
		}
		
		ItemStack hand = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (TreeHarvesterConfigHandler.GENERAL.mustHoldAxeForTreeHarvest.get()) {
			if (!ToolFunctions.isAxe(hand)) {
				return;
			}
		}
		
		if (hand == null) {
			return;
		}
		
		int logcount = TreeHarvesterUtil.isTreeAndReturnLogAmount(world, bpos);
		if (logcount < 0) {
			return;
		}
		
		Item handitem = hand.getItem();
		
		List<BlockPos> logstobreak = TreeHarvesterUtil.getAllLogsToBreak(world, bpos, logcount);
		for (BlockPos logpos : logstobreak) {
			BlockState logstate = world.getBlockState(logpos);
			Block log = logstate.getBlock();
			
			if (log.canHarvestBlock(logstate, world, logpos, player)) {
				world.destroyBlock(logpos, false);
				log.playerDestroy(world, player, logpos, logstate, null, hand);
				if (!player.isCreative()) {
					if (TreeHarvesterConfigHandler.GENERAL.loseDurabilityPerHarvestedLog.get()) {
						handitem.mineBlock(hand, world, logstate, logpos, player);
					}
					if (TreeHarvesterConfigHandler.GENERAL.increaseExhaustionPerHarvestedLog.get()) {
						player.causeFoodExhaustion(0.025F);
					}
				}
			}
		}
		
		if (logstobreak.size() == 0) {
			return;
		}
		
		e.setCanceled(true);
		
		if (TreeHarvesterConfigHandler.GENERAL.enableFastLeafDecay.get() && !TreeHarvesterConfigHandler.GENERAL.instantBreakLeavesAround.get()) {
			List<BlockPos> logs = new ArrayList<BlockPos>();
			List<BlockPos> leaves = new ArrayList<BlockPos>();
			
			Iterator<BlockPos> possiblelogs = BlockPos.betweenClosed(bpos.getX()-8, bpos.getY(), bpos.getZ()-8, bpos.getX()+8, TreeHarvesterUtil.highestleaf.get(bpos), bpos.getZ()+8).iterator();
			while(possiblelogs.hasNext()) {
				BlockPos next = possiblelogs.next();
				if (TreeHarvesterUtil.isTreeLog(world.getBlockState(next).getBlock())) {
					logs.add(next.immutable());
				}
			}
			
			Pair<Integer, Integer> hv = TreeHarvesterUtil.getHorizontalAndVerticalValue(logcount);
			int h = hv.getFirst();
			
			Iterator<BlockPos> possibleleaves = BlockPos.betweenClosed(bpos.getX()-h, bpos.getY(), bpos.getZ()-h, bpos.getX()+h, TreeHarvesterUtil.highestleaf.get(bpos), bpos.getZ()+h).iterator();
			while (possibleleaves.hasNext()) {
				BlockPos next = possibleleaves.next();
				if (TreeHarvesterUtil.isTreeLeaf(world.getBlockState(next).getBlock())) {
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
			TreeHarvesterUtil.highestleaf.remove(bpos);
		}
	}
}