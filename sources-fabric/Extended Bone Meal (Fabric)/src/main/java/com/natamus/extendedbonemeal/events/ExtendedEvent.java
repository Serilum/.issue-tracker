/*
 * This is the latest source code of Extended Bone Meal.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.extendedbonemeal.events;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.CropFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class ExtendedEvent {
	public static boolean onBoneMeal(Player player, Level world, BlockPos cpos, BlockState state, ItemStack stack) {
		if (world.isClientSide) {
			return true;
		}

		if (player == null) {
			return true;
		}

		if (!player.isCrouching()) {
			return true;
		}
		
		ItemStack hand = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!hand.getItem().equals(Items.BONE_MEAL)) {
			return true;
		}
		
		Block block = state.getBlock();
		if (block instanceof BonemealableBlock == false || block instanceof SaplingBlock || block.equals(Blocks.GRASS_BLOCK) || block instanceof TallGrassBlock || block instanceof MushroomBlock) { // || block.equals(Blocks.sap) || block.equals(Blocks.GRASS)) {
			return true;
		}
		
		if (CropFunctions.growCrop(world, player, state, cpos)) {
			return false;
		}
		
		return true;
	}
	
	public static InteractionResult onNetherwartClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		ItemStack handstack = player.getItemInHand(hand);
		if (!handstack.getItem().equals(Items.BONE_MEAL)) {
			return InteractionResult.PASS;
		}
		
		BlockPos targetpos = BlockPosFunctions.getBlockPosFromHitResult(hitResult);
		BlockState state = world.getBlockState(targetpos);
		Block block = state.getBlock();
		if (block.equals(Blocks.AIR)) {
			targetpos = targetpos.below().immutable();
			state = world.getBlockState(targetpos);
			block = state.getBlock();
		}
		
		if (block.equals(Blocks.NETHER_WART)) {
			if (!CropFunctions.growCrop(world, player, state, targetpos)) {
				return InteractionResult.PASS;
			}
		}
		else if (block.equals(Blocks.CACTUS)) {
			if (!CropFunctions.growCactus(world, targetpos)) {
				return InteractionResult.PASS;
			}
		}
		else if (block.equals(Blocks.SUGAR_CANE)) {
			if (!CropFunctions.growSugarcane(world, targetpos)) {
				return InteractionResult.PASS;
			}
		}
		else if (block.equals(Blocks.VINE)) {
			if (!CropFunctions.growVine(world, targetpos)) {
				return InteractionResult.PASS;
			}
		}
		else {
			return InteractionResult.PASS;
		}
		
		if (!player.isCreative()) {
			handstack.shrink(1);
		}
		
		return InteractionResult.SUCCESS;
	}
}