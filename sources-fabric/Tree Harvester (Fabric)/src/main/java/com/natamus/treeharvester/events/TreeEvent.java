/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.19.1, mod version: 5.4.
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
import com.natamus.collective_fabric.functions.*;
import com.natamus.treeharvester.config.ConfigHandler;
import com.natamus.treeharvester.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TreeEvent {
	private static HashMap<Level, CopyOnWriteArrayList<List<BlockPos>>> processleaves = new HashMap<Level, CopyOnWriteArrayList<List<BlockPos>>>();
	private static HashMap<Pair<Level, Player>, Pair<Date, Integer>> harvestSpeedCache = new HashMap<Pair<Level, Player>, Pair<Date, Integer>>();
	
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
				for (int i = 0; i < ConfigHandler.amountOfLeavesBrokenPerTick.getValue(); i++) {
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
					if (ConfigHandler.replaceSaplingOnTreeHarvest.getValue()) {
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
	
	public static boolean onTreeHarvest(Level world, Player player, BlockPos bpos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return true;
		}

		Block block = world.getBlockState(bpos).getBlock();
		if (!CompareBlockFunctions.isTreeLog(block) && !Util.isGiantMushroomStemBlock(block)) {
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
		Item handitem = hand.getItem();
		if (ConfigHandler.mustHoldAxeForTreeHarvest.getValue()) {
			if (!ToolFunctions.isAxe(hand)) {
				return true;
			}

			if (!Util.allowedAxes.contains(handitem)) {
				return true;
			}
		}
		
		if (ConfigHandler.automaticallyFindBottomBlock.getValue()) {
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
			return true;
		}
		
		int durabilitylosecount = (int)Math.ceil(1.0 / ConfigHandler.loseDurabilityModifier.getValue());
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

			BlockFunctions.dropBlock(world, logpos);
			//ForgeEventFactory.onEntityDestroyBlock(player, logpos, logstate);

			if (!player.isCreative()) {
				if (ConfigHandler.loseDurabilityPerHarvestedLog.getValue()) {
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
				if (ConfigHandler.increaseExhaustionPerHarvestedLog.getValue()) {
					player.causeFoodExhaustion(0.025F * ConfigHandler.increaseExhaustionModifier.getValue().floatValue());
				}
			}
		}
		
		if (logstobreak.size() == 0) {
			return true;
		}
		
		if (ConfigHandler.enableFastLeafDecay.getValue() && !ConfigHandler.instantBreakLeavesAround.getValue()) {
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
				if (!leafblock.equals(nextblock) && !(ConfigHandler.enableNetherTrees.getValue() && nextblock.equals(Blocks.SHROOMLIGHT))) {
					continue;
				}

				if (CompareBlockFunctions.isTreeLeaf(nextblock, ConfigHandler.enableNetherTrees.getValue()) || Util.isGiantMushroomLeafBlock(nextblock)) {
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

			if (ConfigHandler.increaseHarvestingTimePerLog.getValue()) {
				Pair<Level, BlockPos> keypair = new Pair<Level, BlockPos>(world, bpos);
				harvestSpeedCache.remove(keypair);
			}
		}

		return false;
	}

	public static float onHarvestBreakSpeed(Level world, Player player, float digSpeed, BlockState state) {
		if (!ConfigHandler.increaseHarvestingTimePerLog.getValue()) {
			return digSpeed;
		}

		Block block = state.getBlock();
		if (!CompareBlockFunctions.isTreeLog(block) && !Util.isGiantMushroomStemBlock(block)) {
			return digSpeed;
		}

		if (ConfigHandler.treeHarvestWithoutSneak.getValue()) {
			if (player.isShiftKeyDown()) {
				return digSpeed;
			}
		}
		else {
			if (!player.isShiftKeyDown()) {
				return digSpeed;
			}
		}

		ItemStack hand = player.getItemInHand(InteractionHand.MAIN_HAND);
		Item handitem = hand.getItem();
		if (ConfigHandler.mustHoldAxeForTreeHarvest.getValue()) {
			if (!ToolFunctions.isAxe(hand)) {
				return digSpeed;
			}

			if (!Util.allowedAxes.contains(handitem)) {
				return digSpeed;
			}
		}

		int logcount = -1;

		Date now = new Date();
		Pair<Level, Player> keypair = new Pair<Level, Player>(world, player);
		if (harvestSpeedCache.containsKey(keypair)) {
			Pair<Date, Integer> valuepair = harvestSpeedCache.get(keypair);
			long ms = (now.getTime()-valuepair.getFirst().getTime());

			if (ms < 1000) {
				logcount = valuepair.getSecond();
			}
			else {
				harvestSpeedCache.remove(keypair);
			}
		}

		BlockPos bpos = null;

		HitResult hitResult = player.pick(20.0D, 0.0F, false);
		if (hitResult.getType() == HitResult.Type.BLOCK) {
			bpos = ((BlockHitResult)hitResult).getBlockPos();
		}

		if (bpos == null) {
			return digSpeed;
		}

		if (logcount < 0) {
			if (Util.isTreeAndReturnLogAmount(world, bpos) < 0) {
				return digSpeed;
			}

			logcount = BlockPosFunctions.getBlocksNextToEachOtherMaterial(world, bpos, Arrays.asList(Material.WOOD), 30).size(); // Util.isTreeAndReturnLogAmount(world, bpos);
			if (logcount <= 0) {
				return digSpeed;
			}

			harvestSpeedCache.put(keypair, new Pair<Date, Integer>(now, logcount));
		}

		return digSpeed/(1+(logcount * ConfigHandler.increasedHarvestingTimePerLogModifier.getValue().floatValue()));
	}
}