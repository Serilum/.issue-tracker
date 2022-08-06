/*
 * This is the latest source code of Grass Seeds.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.grassseeds.events;

import com.natamus.collective_fabric.functions.BlockPosFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.HitResult;

public class GrassEvent {
	public static InteractionResult onDirtClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		ItemStack handstack = player.getItemInHand(hand);
		if (!handstack.getItem().equals(Items.WHEAT_SEEDS)) {
			return InteractionResult.PASS;
		}
		
		BlockPos cpos = BlockPosFunctions.getBlockPosFromHitResult(hitResult).below();
		Block block = world.getBlockState(cpos).getBlock();
		BlockPos up = cpos.above();
		Block upBlock = world.getBlockState(up).getBlock();

		if (upBlock.equals(Blocks.FARMLAND)) {
			return InteractionResult.PASS;
		}

		if (block.equals(Blocks.DIRT)) {
			world.setBlockAndUpdate(cpos, Blocks.GRASS_BLOCK.defaultBlockState());
		}
		else if (block.equals(Blocks.GRASS_BLOCK)) {
			if (upBlock.equals(Blocks.AIR)) {
				world.setBlockAndUpdate(up, Blocks.GRASS.defaultBlockState());
			}
			else if (upBlock.equals(Blocks.GRASS)) {
				upgradeGrass(world, up);
			}
			else {
				return InteractionResult.PASS;
			}
		}
		else if (block.equals(Blocks.GRASS)) {
			upgradeGrass(world, cpos);
		}
		else {
			return InteractionResult.PASS;
		}
		
		if (!player.isCreative()) {
			handstack.shrink(1);
		}
		
		return InteractionResult.SUCCESS;
	}
	
	private static void upgradeGrass(Level world, BlockPos pos) {
	      DoublePlantBlock blockdoubleplant = (DoublePlantBlock)Blocks.TALL_GRASS;
	      BlockState doubleplantstate = blockdoubleplant.defaultBlockState();
	      if (doubleplantstate.canSurvive(world, pos) && world.isEmptyBlock(pos.above())) {
	         world.setBlock(pos, (BlockState)blockdoubleplant.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 2);
	         world.setBlock(pos.above(), (BlockState)blockdoubleplant.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
	      }
	}
}
