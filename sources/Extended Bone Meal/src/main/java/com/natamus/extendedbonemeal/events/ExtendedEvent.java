/*
 * This is the latest source code of Extended Bone Meal.
 * Minecraft version: 1.19.2, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.extendedbonemeal.events;

import com.natamus.collective.functions.CropFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
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
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ExtendedEvent {
	@SubscribeEvent
	public void onBoneMeal(BonemealEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Player player = e.getEntity();
		if (player == null) {
			return;
		}

		if (!player.isShiftKeyDown()) {
			return;
		}
		
		ItemStack hand = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!hand.getItem().equals(Items.BONE_MEAL)) {
			return;
		}
		
		BlockPos cpos = e.getPos();
		BlockState state = world.getBlockState(cpos);
		Block block = state.getBlock();
		if (block instanceof BonemealableBlock == false || block instanceof SaplingBlock || block.equals(Blocks.GRASS_BLOCK) || block instanceof TallGrassBlock || block instanceof MushroomBlock) { // || block.equals(Blocks.sap) || block.equals(Blocks.GRASS)) {
			return;
		}
		
		if (CropFunctions.growCrop(world, player, state, cpos)) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onNetherwartClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Player player = e.getEntity();
		ItemStack hand = e.getItemStack();
		if (!hand.getItem().equals(Items.BONE_MEAL)) {
			return;
		}
		
		BlockPos targetpos = e.getPos();
		BlockState state = world.getBlockState(targetpos);
		Block block = state.getBlock();
		if (block.equals(Blocks.NETHER_WART)) {
			if (!CropFunctions.growCrop(world, player, state, targetpos)) {
				return;
			}
		}
		else if (block.equals(Blocks.CACTUS)) {
			if (!CropFunctions.growCactus(world, targetpos)) {
				return;
			}
		}
		else if (block.equals(Blocks.SUGAR_CANE)) {
			if (!CropFunctions.growSugarcane(world, targetpos)) {
				return;
			}
		}
		else if (block.equals(Blocks.VINE)) {
			if (!CropFunctions.growVine(world, targetpos)) {
				return;
			}
		}
		else {
			return;
		}
		
		if (!player.isCreative()) {
			hand.shrink(1);
		}
	}
}