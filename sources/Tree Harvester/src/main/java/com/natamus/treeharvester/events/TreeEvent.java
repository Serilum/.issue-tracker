/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.17.1, mod version: 2.8.
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
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.ToolFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.treeharvester.config.ConfigHandler;
import com.natamus.treeharvester.util.Util;

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
public class TreeEvent {
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
					if (ConfigHandler.GENERAL.replaceSaplingIfBottomLogIsBroken.get()) {
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
	
	@SubscribeEvent
	public void onTreeHarvest(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockPos bpos = e.getPos();
		Block block = world.getBlockState(bpos).getBlock();
		if (!Util.isTreeLog(block)) {
			return;
		}
		
		Player player = e.getPlayer();
		if (ConfigHandler.GENERAL.treeHarvestWithoutSneak.get()) {
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
		if (ConfigHandler.GENERAL.mustHoldAxeForTreeHarvest.get()) {
			if (!ToolFunctions.isAxe(hand)) {
				return;
			}
		}
		
		if (hand == null) {
			return;
		}
		
		int logcount = Util.isTreeAndReturnLogAmount(world, bpos);
		if (logcount < 0) {
			return;
		}
		
		Item handitem = hand.getItem();
		
		List<BlockPos> logstobreak = Util.getAllLogsToBreak(world, bpos, logcount);
		for (BlockPos logpos : logstobreak) {
			BlockState logstate = world.getBlockState(logpos);
			Block log = logstate.getBlock();
			
			if (log.canHarvestBlock(logstate, world, logpos, player)) {
				world.destroyBlock(logpos, false);
				log.playerDestroy(world, player, logpos, logstate, null, hand);
				if (!player.isCreative()) {
					if (ConfigHandler.GENERAL.loseDurabilityPerHarvestedLog.get()) {
						handitem.mineBlock(hand, world, logstate, logpos, player);
					}
					if (ConfigHandler.GENERAL.increaseExhaustionPerHarvestedLog.get()) {
						player.causeFoodExhaustion(0.025F);
					}
				}
			}
		}
		
		if (logstobreak.size() == 0) {
			return;
		}
		
		e.setCanceled(true);
		
		if (ConfigHandler.GENERAL.enableFastLeafDecay.get() && !ConfigHandler.GENERAL.instantBreakLeavesAround.get()) {
			List<BlockPos> logs = new ArrayList<BlockPos>();
			List<BlockPos> leaves = new ArrayList<BlockPos>();
			
			Iterator<BlockPos> possiblelogs = BlockPos.betweenClosed(bpos.getX()-8, bpos.getY(), bpos.getZ()-8, bpos.getX()+8, Util.highestleaf.get(bpos), bpos.getZ()+8).iterator();
			while(possiblelogs.hasNext()) {
				BlockPos next = possiblelogs.next();
				if (Util.isTreeLog(world.getBlockState(next).getBlock())) {
					logs.add(next.immutable());
				}
			}
			
			Pair<Integer, Integer> hv = Util.getHorizontalAndVerticalValue(logcount);
			int h = hv.getFirst();
			
			Iterator<BlockPos> possibleleaves = BlockPos.betweenClosed(bpos.getX()-h, bpos.getY(), bpos.getZ()-h, bpos.getX()+h, Util.highestleaf.get(bpos), bpos.getZ()+h).iterator();
			while (possibleleaves.hasNext()) {
				BlockPos next = possibleleaves.next();
				if (Util.isTreeLeaf(world.getBlockState(next).getBlock())) {
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
	}
}