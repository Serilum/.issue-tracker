/*
 * This is the latest source code of Better Beacon Placement.
 * Minecraft version: 1.19.2, mod version: 1.9.
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

package com.natamus.betterbeaconplacement.events;

import com.natamus.betterbeaconplacement.config.ConfigHandler;
import com.natamus.betterbeaconplacement.util.Util;
import com.natamus.collective_fabric.functions.BlockFunctions;
import com.natamus.collective_fabric.functions.BlockPosFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class BeaconEvent {
	public static InteractionResult onBeaconClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		ItemStack handstack = player.getItemInHand(hand);
		if (!BlockFunctions.isOneOfBlocks(Util.mineralblocks, handstack)) {
			return InteractionResult.PASS;
		}
		
		BlockPos cpos = BlockPosFunctions.getBlockPosFromHitResult(hitResult);
		if (!world.getBlockState(cpos).getBlock().equals(Blocks.BEACON)) {
			return InteractionResult.PASS;
		}
		
		boolean set = false;
		while (handstack.getCount() > 0) {
			BlockPos nextpos = Util.getNextLocation(world, cpos);
			if (nextpos == null) {
				if (set) {
					return InteractionResult.FAIL;
				}
				return InteractionResult.PASS;
			}
			
			Block block = world.getBlockState(nextpos).getBlock();
			if (ConfigHandler.dropReplacedBlockTopBeacon.getValue()) {
				if (!block.equals(Blocks.AIR) && !player.isCreative()) {
					ItemEntity ei = new ItemEntity(world, cpos.getX(), cpos.getY()+2, cpos.getZ(), new ItemStack(block, 1));
					world.addFreshEntity(ei);
				}
			}
			
			if (!player.isCreative()) {
				handstack.shrink(1);
			}
			
			world.setBlockAndUpdate(nextpos, Block.byItem(handstack.getItem()).defaultBlockState());
			
			set = true;
			if (!player.isCrouching()) {
				break;
			}
		}
		
		return InteractionResult.FAIL;
	}
	
	public static void onBlockBreak(Level world, Player player, BlockPos bpos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return;
		}
		
		if (!ConfigHandler.breakBeaconBaseBlocks.getValue()) {
			return;
		}
		
		if (!state.getBlock().equals(Blocks.BEACON)) {
			return;
		}
		
		Util.processAllBaseBlocks(world, bpos, player.isCreative());
	}
}
