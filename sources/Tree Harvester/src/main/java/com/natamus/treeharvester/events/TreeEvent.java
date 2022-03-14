/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.18.2, mod version: 5.1.
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

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.*;
import com.natamus.treeharvester.config.ConfigHandler;
import com.natamus.treeharvester.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@EventBusSubscriber
public class TreeEvent {
	private static HashMap<Level, CopyOnWriteArrayList<List<BlockPos>>> processleaves = new HashMap<Level, CopyOnWriteArrayList<List<BlockPos>>>();
	private static HashMap<Pair<Level, BlockPos>, Pair<Date, Integer>> harvestSpeedCache = new HashMap<Pair<Level, BlockPos>, Pair<Date, Integer>>();
	
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
				for (int i = 0; i < ConfigHandler.GENERAL.amountOfLeavesBrokenPerTick.get(); i++) {
					if (leaves.isEmpty()) {
						break;
					}

					BlockPos tr = leaves.get(0);

					BlockFunctions.dropBlock(world, tr);

					leaves.remove(0);
					lasttr = tr.immutable();
				}
			}
			
			if (leaves.size() == 0) {
				processleaves.get(world).remove(leaves);
				if (lasttr != null) {
					if (ConfigHandler.GENERAL.replaceSaplingOnTreeHarvest.get()) {
						if (Util.lowerlogs.size() > 0) {
							BlockPos lowerlasttrpos = new BlockPos(lasttr.getX(), 1, lasttr.getZ());
							for (Pair<BlockPos, CopyOnWriteArrayList<BlockPos>> pair : Util.lowerlogs) {
								BlockPos breakpos = pair.getFirst();
								if (BlockPosFunctions.withinDistance(lowerlasttrpos, new BlockPos(breakpos.getX(), 1, breakpos.getZ()), 5)) {
									Util.replaceSapling(world, breakpos, pair.getSecond(), 1, null);
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
		if (!CompareBlockFunctions.isTreeLog(block) && !Util.isGiantMushroomStemBlock(block)) {
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
		Item handitem = hand.getItem();
		if (ConfigHandler.GENERAL.mustHoldAxeForTreeHarvest.get()) {
			if (!ToolFunctions.isAxe(hand)) {
				return;
			}

			if (!Util.allowedAxes.contains(handitem)) {
				return;
			}
		}
		
		if (ConfigHandler.GENERAL.automaticallyFindBottomBlock.get()) {
			BlockPos temppos = bpos.immutable();
			while (world.getBlockState(temppos.below()).getBlock().equals(block)) {
				temppos = temppos.below().immutable();
			}

			for (BlockPos belowpos : BlockPos.betweenClosed(temppos.getX()-1, temppos.getY()-1, temppos.getZ()-1, temppos.getX()+1, temppos.getY()-1, temppos.getZ()+1)) {
				if (world.getBlockState(belowpos).getBlock().equals(block)) {
					temppos = belowpos.immutable();
					while (world.getBlockState(temppos.below()).getBlock().equals(block)) {
						temppos = temppos.below().immutable();
					}
					break;
				}
			}

			bpos = temppos.immutable();
		}
		
		int logcount = Util.isTreeAndReturnLogAmount(world, bpos);
		if (logcount < 0) {
			return;
		}
		
		int durabilitylosecount = (int)Math.ceil(1.0 / ConfigHandler.GENERAL.loseDurabilityModifier.get());
		int durabilitystartcount = -1;

		ServerPlayer serverPlayer = (ServerPlayer)player;

		BlockPos highestlog = bpos.immutable();
		List<BlockPos> logstobreak = Util.getAllLogsToBreak(world, bpos, logcount, block);
		for (BlockPos logpos : logstobreak) {
			if (logpos.getY() > highestlog.getY()) {
				highestlog = logpos.immutable();
			}

			BlockState logstate = world.getBlockState(logpos);
			Block log = logstate.getBlock();
			
			if (log.canHarvestBlock(logstate, world, logpos, player)) {
				BlockFunctions.dropBlock(world, logpos);
				ForgeEventFactory.onEntityDestroyBlock(player, logpos, logstate);

				if (!player.isCreative()) {
					if (ConfigHandler.GENERAL.loseDurabilityPerHarvestedLog.get()) {
						if (durabilitystartcount == -1) {
							durabilitystartcount = durabilitylosecount;
							ItemFunctions.itemHurtBreakAndEvent(hand, serverPlayer, InteractionHand.MAIN_HAND, 1);
						}
						else {
							durabilitylosecount -= 1;
							
							if (durabilitylosecount == 0) {
								ItemFunctions.itemHurtBreakAndEvent(hand, serverPlayer, InteractionHand.MAIN_HAND, 1);
								durabilitylosecount = durabilitystartcount;
							}	
						}
					}
					if (ConfigHandler.GENERAL.increaseExhaustionPerHarvestedLog.get()) {
						player.causeFoodExhaustion(0.025F * ConfigHandler.GENERAL.increaseExhaustionModifier.get().floatValue());
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

			for (BlockPos next : BlockPos.betweenClosed(bpos.getX() - 8, bpos.getY(), bpos.getZ() - 8, bpos.getX() + 8, Util.highestleaf.get(bpos), bpos.getZ() + 8)) {
				Block nextblock = world.getBlockState(next).getBlock();
				if (CompareBlockFunctions.isTreeLog(nextblock) || Util.isGiantMushroomStemBlock(nextblock)) {
					if (nextblock.equals(block)) {
						logs.add(next.immutable());
					}
				}
			}

			Pair<Integer, Integer> hv = Util.getHorizontalAndVerticalValue(world, bpos, block, logcount);
			int h = hv.getFirst();

			CopyOnWriteArrayList<BlockPos> leftoverleaves = new CopyOnWriteArrayList<BlockPos>();

			Block leafblock = world.getBlockState(highestlog.above()).getBlock();
			for (BlockPos next : BlockPos.betweenClosed(bpos.getX() - h, bpos.getY(), bpos.getZ() - h, bpos.getX() + h, Util.highestleaf.get(bpos), bpos.getZ() + h)) {
				Block nextblock = world.getBlockState(next).getBlock();
				if (!leafblock.equals(nextblock) && !(ConfigHandler.GENERAL.enableNetherTrees.get() && nextblock.equals(Blocks.SHROOMLIGHT))) {
					continue;
				}

				if (CompareBlockFunctions.isTreeLeaf(nextblock, ConfigHandler.GENERAL.enableNetherTrees.get()) || Util.isGiantMushroomLeafBlock(nextblock)) {
					boolean logclose = false;
					for (BlockPos log : logs) {
						double distance = log.distSqr(next);
						if (BlockPosFunctions.withinDistance(log, next, 3)) {
							logclose = true;
							break;
						}
					}

					if (!logclose) {
						leaves.add(next.immutable());
					}
					else {
						leftoverleaves.add(next.immutable());
					}
				}
			}

			for (BlockPos leftoverleaf : leftoverleaves) {
				if (leftoverleaves.isEmpty()) {
					break;
				}

				Pair<Boolean, List<BlockPos>> connectedpair = Util.isConnectedToLogs(world, leftoverleaf);
				if (connectedpair.getFirst()) {
					for (BlockPos connectedpos : connectedpair.getSecond()) {
						leftoverleaves.remove(connectedpos);
					}
				}
				else {
					for (BlockPos connectedpos : connectedpair.getSecond()) {
						if (!leaves.contains(connectedpos)) {
							leaves.add(connectedpos.immutable());
						}

						leftoverleaves.remove(connectedpos);
					}
				}
			}
			
			Collections.shuffle(leaves);
			processleaves.get(world).add(leaves);
			Util.highestleaf.remove(bpos);

			if (ConfigHandler.GENERAL.increaseHarvestingTimePerLog.get()) {
				Pair<Level, BlockPos> keypair = new Pair<Level, BlockPos>(world, bpos);
				harvestSpeedCache.remove(keypair);
			}
		}
	}

	@SubscribeEvent
	public void onHarvestBreakSpeed(PlayerEvent.BreakSpeed e) {
		if (!ConfigHandler.GENERAL.increaseHarvestingTimePerLog.get()) {
			return;
		}

		Player player = e.getPlayer();
		Level world = player.getCommandSenderWorld();

		BlockPos bpos = e.getPos();
		Block block = world.getBlockState(bpos).getBlock();
		if (!CompareBlockFunctions.isTreeLog(block) && !Util.isGiantMushroomStemBlock(block)) {
			return;
		}

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
		Item handitem = hand.getItem();
		if (ConfigHandler.GENERAL.mustHoldAxeForTreeHarvest.get()) {
			if (!ToolFunctions.isAxe(hand)) {
				return;
			}

			if (!Util.allowedAxes.contains(handitem)) {
				return;
			}
		}

		int logcount = -1;

		Date now = new Date();
		Pair<Level, BlockPos> keypair = new Pair<Level, BlockPos>(world, bpos);
		if (harvestSpeedCache.containsKey(keypair)) {
			Pair<Date, Integer> valuepair = harvestSpeedCache.get(keypair);
			long ms = (now.getTime()-valuepair.getFirst().getTime());

			if (ms < 5000) {
				logcount = valuepair.getSecond();
			}
			else {
				harvestSpeedCache.remove(keypair);
			}
		}

		if (logcount < 0) {
			if (Util.isTreeAndReturnLogAmount(world, bpos) < 0) {
				return;
			}

			logcount = BlockPosFunctions.getBlocksNextToEachOtherMaterial(world, bpos, Arrays.asList(Material.WOOD), 30).size(); // Util.isTreeAndReturnLogAmount(world, bpos);
			if (logcount <= 0) {
				return;
			}

			harvestSpeedCache.put(keypair, new Pair<Date, Integer>(now, logcount));
		}

		e.setNewSpeed(e.getOriginalSpeed()/(1+(logcount * ConfigHandler.GENERAL.increasedHarvestingTimePerLogModifier.get().floatValue())));
	}
}