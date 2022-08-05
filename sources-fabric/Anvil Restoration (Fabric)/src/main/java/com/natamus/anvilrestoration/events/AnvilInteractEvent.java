/*
 * This is the latest source code of Anvil Restoration.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.anvilrestoration.events;

import com.natamus.collective_fabric.functions.BlockPosFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class AnvilInteractEvent {
	public static InteractionResult onAnvilClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		ItemStack handstack = player.getItemInHand(hand);
		Item item = handstack.getItem();
		if (!item.equals(Items.IRON_INGOT) && !item.equals(Items.OBSIDIAN)) {
			return InteractionResult.PASS;
		}
		
		BlockPos cpos = BlockPosFunctions.getBlockPosFromHitResult(hitResult);
		BlockState state = world.getBlockState(cpos);
		Block block = state.getBlock();
		
		BlockState newstate;
		if (block.equals(Blocks.ANVIL) && item.equals(Items.OBSIDIAN)) {
			newstate = Blocks.CHIPPED_ANVIL.defaultBlockState();
		}
		else if (block.equals(Blocks.CHIPPED_ANVIL)) {
			if (item.equals(Items.IRON_INGOT)) {
				newstate = Blocks.ANVIL.defaultBlockState();
			}
			else { // obsidian
				newstate = Blocks.DAMAGED_ANVIL.defaultBlockState();
			}
		}
		else if (block.equals(Blocks.DAMAGED_ANVIL) && item.equals(Items.IRON_INGOT)) {
			newstate = Blocks.CHIPPED_ANVIL.defaultBlockState();
		}
		else {
			return InteractionResult.PASS;
		}
		
		Direction rotation = state.getValue(AnvilBlock.FACING);
		world.setBlock(cpos, newstate.setValue(AnvilBlock.FACING, rotation), 3);
		world.playSound(null, cpos.getX(), cpos.getY(), cpos.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.5F, 1.0F);
		
		if (!player.isCreative() && item.equals(Items.IRON_INGOT)) {
			handstack.shrink(1);
		}
		
		return InteractionResult.SUCCESS;
	}
}
