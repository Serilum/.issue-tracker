/*
 * This is the latest source code of Better Conduit Placement.
 * Minecraft version: 1.19.2, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.betterconduitplacement.events;

import com.natamus.betterconduitplacement.config.ConfigHandler;
import com.natamus.betterconduitplacement.util.Util;
import com.natamus.collective_fabric.functions.BlockFunctions;
import com.natamus.collective_fabric.functions.BlockPosFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ConduitEvent {
	public static InteractionResultHolder<ItemStack> onWaterClick(Player player, Level world, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (world.isClientSide) {
			return InteractionResultHolder.pass(itemstack);
		}
		
		if (!itemstack.getItem().equals(Items.CONDUIT)) {
			return InteractionResultHolder.pass(itemstack);
		}
		
		Vec3 look = player.getLookAngle();
		float distance = 2.0F;
		double dx = player.getX() + (look.x * distance);
		double dy = player.getY() + (look.y * distance) + 2;
		double dz = player.getZ() + (look.z * distance);
		
		BlockPos frontpos = new BlockPos(dx, dy, dz);
		
		if (!world.getBlockState(frontpos).getBlock().equals(Blocks.WATER)) {
			return InteractionResultHolder.pass(itemstack);
		}
		
		if (!player.isCreative()) {
			itemstack.shrink(1);
		}
		
		world.setBlockAndUpdate(frontpos, Blocks.CONDUIT.defaultBlockState());
		return InteractionResultHolder.success(itemstack);
	}
	
	public static InteractionResult onConduitClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		BlockPos cpos = BlockPosFunctions.getBlockPosFromHitResult(hitResult);
		if (!world.getBlockState(cpos).getBlock().equals(Blocks.CONDUIT)) {
			return InteractionResult.PASS;
		}
		
		ItemStack handstack = player.getItemInHand(hand);
		if (!BlockFunctions.isOneOfBlocks(Util.conduitblocks, handstack)) {
			return InteractionResult.PASS;
		}
		
		boolean set = false;
		while (handstack.getCount() > 0) {
			BlockPos nextpos = Util.getNextLocation(world, cpos);
			if (nextpos == null) {
				break;
			}
			
			Block block = world.getBlockState(nextpos).getBlock();
			if (ConfigHandler.dropReplacedBlockTopConduit.getValue()) {
				if (!block.equals(Blocks.AIR) && !block.equals(Blocks.WATER)) {
					ItemEntity ei = new ItemEntity(world, cpos.getX(), cpos.getY()+1, cpos.getZ(), new ItemStack(block, 1));
					world.addFreshEntity(ei);
				}
			}
			
			if (!player.isCreative()) {
				handstack.shrink(1);
			}
			
			world.setBlockAndUpdate(nextpos, Block.byItem(handstack.getItem()).defaultBlockState());
			
			set = true;
			if (!player.isShiftKeyDown()) {
				break;
			}
		}
		
		if (set) {
			return InteractionResult.FAIL;
		}
		
		return InteractionResult.PASS;
	}
	
	public static void onBlockBreak(Level world, Player player, BlockPos bpos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return;
		}
		
		if (!ConfigHandler.breakConduitBlocks.getValue()) {
			return;
		}
		
		if (!state.getBlock().equals(Blocks.CONDUIT)) {
			return;
		}
		
		Util.destroyAllConduitBlocks(world, bpos);
	}
}
