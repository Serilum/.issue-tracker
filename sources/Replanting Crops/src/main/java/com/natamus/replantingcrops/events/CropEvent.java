/*
 * This is the latest source code of Replanting Crops.
 * Minecraft version: 1.19.2, mod version: 2.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.replantingcrops.events;

import java.util.HashMap;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.replantingcrops.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CropEvent {
	private static HashMap<BlockPos, Item> checkreplant = new HashMap<BlockPos, Item>();
	private static HashMap<BlockPos, BlockState> cocoaStates = new HashMap<BlockPos, BlockState>();
	
	@SubscribeEvent
	public void onHarvest(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		Player player = e.getPlayer();
		if (player == null) {
			return;
		}
		
		InteractionHand hand = null;
		if (ConfigHandler.GENERAL.mustHoldHoeForReplanting.get()) {
			hand = InteractionHand.MAIN_HAND;
			if (player.getMainHandItem().getItem() instanceof HoeItem == false) {
				hand = InteractionHand.OFF_HAND;
				if (player.getOffhandItem().getItem() instanceof HoeItem == false) {
					return;
				}
			}
		}
		
		if (player.isShiftKeyDown()) {
			return;
		}
		
		BlockPos hpos = e.getPos().immutable();
		BlockState state = world.getBlockState(hpos);
		Block block = state.getBlock();
		
		if (block.equals(Blocks.WHEAT)) {
			checkreplant.put(hpos, Items.WHEAT_SEEDS);
		}
		else if (block.equals(Blocks.CARROTS)) {
			checkreplant.put(hpos, Items.CARROT);
		}
		else if (block.equals(Blocks.POTATOES)) {
			checkreplant.put(hpos, Items.POTATO);
		}
		else if (block.equals(Blocks.BEETROOTS)) {
			checkreplant.put(hpos, Items.BEETROOT_SEEDS);
		}
		else if (block.equals(Blocks.NETHER_WART)) {
			checkreplant.put(hpos, Items.NETHER_WART);
		}
		else if (block.equals(Blocks.COCOA)) {
			cocoaStates.put(hpos, state);
			checkreplant.put(hpos, Items.COCOA_BEANS);
		}
		else {
			return;
		}
		
		if (!player.isCreative()) {
			player.getItemInHand(hand).hurt(1, GlobalVariables.randomSource, (ServerPlayer)player);
		}
	}
	
	@SubscribeEvent
	public void onHarvest(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof ItemEntity == false) {
			return;
		}
		
		BlockPos ipos = entity.blockPosition();
		if (!checkreplant.containsKey(ipos)) {
			return;
		}
		
		ItemEntity itementity = (ItemEntity)entity;
		ItemStack itemstack = itementity.getItem();
		Item item = itemstack.getItem();
		if (item.equals(Items.WHEAT_SEEDS)) {
			world.setBlockAndUpdate(ipos, Blocks.WHEAT.defaultBlockState());
		}
		else if (item.equals(Items.CARROT)) {
			world.setBlockAndUpdate(ipos, Blocks.CARROTS.defaultBlockState());
		}
		else if (item.equals(Items.POTATO)) {
			world.setBlockAndUpdate(ipos, Blocks.POTATOES.defaultBlockState());
		}
		else if (item.equals(Items.BEETROOT_SEEDS)) {
			world.setBlockAndUpdate(ipos, Blocks.BEETROOTS.defaultBlockState());
		}
		else if (item.equals(Items.NETHER_WART)) {
			world.setBlockAndUpdate(ipos, Blocks.NETHER_WART.defaultBlockState());
		}
		else if (item.equals(Items.COCOA_BEANS)) {
			if (!cocoaStates.containsKey(ipos)) {
				checkreplant.remove(ipos);
				return;
			}
			world.setBlockAndUpdate(ipos, cocoaStates.get(ipos).setValue(CocoaBlock.AGE, 0));
			cocoaStates.remove(ipos);
		}
		else {
			return;
		}
		
		checkreplant.remove(ipos);
		
		if (itemstack.getCount() > 1) {
			itemstack.shrink(1);
		}
		else {
			e.setCanceled(true);
		}
	}
}
