/*
 * This is the latest source code of Replanting Crops.
 * Minecraft version: 1.19.2, mod version: 4.0.
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

package com.natamus.replantingcrops.events;

import com.natamus.collective_fabric.functions.ItemFunctions;
import com.natamus.collective_fabric.functions.ToolFunctions;
import com.natamus.replantingcrops.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class CropEvent {
	private static HashMap<BlockPos, Block> checkreplant = new HashMap<BlockPos, Block>();
	private static HashMap<BlockPos, BlockState> cocoaStates = new HashMap<BlockPos, BlockState>();
	
	public static boolean onHarvest(Level world, Player player, BlockPos hpos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return true;
		}
		
		if (player == null) {
			return true;
		}
		
		InteractionHand hand = null;
		if (ConfigHandler.mustHoldHoeForReplanting.getValue()) {
			hand = InteractionHand.MAIN_HAND;
			if (!ToolFunctions.isHoe(player.getMainHandItem())) {
				hand = InteractionHand.OFF_HAND;
				if (!ToolFunctions.isHoe(player.getOffhandItem())) {
					return true;
				}
			}
		}
		
		if (player.isShiftKeyDown()) {
			return true;
		}
		
		Block block = state.getBlock();

		if (block instanceof CropBlock) {
			checkreplant.put(hpos, block);
		}
		else if (block.equals(Blocks.NETHER_WART)) {
			checkreplant.put(hpos, block);
		}
		else if (block.equals(Blocks.COCOA)) {
			cocoaStates.put(hpos, state);
			checkreplant.put(hpos, block);
		}
		else {
			return true;
		}
		
		if (!player.isCreative()) {
			ItemFunctions.itemHurtBreakAndEvent(player.getItemInHand(hand), (ServerPlayer)player, hand, 1);
		}
		
		return true;
	}
	
	public static void onHarvest(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}
		
		if (!(entity instanceof ItemEntity)) {
			return;
		}
		
		BlockPos ipos = entity.blockPosition();
		if (!checkreplant.containsKey(ipos)) {
			return;
		}

		Block preblock = checkreplant.get(ipos);

		Item compareitem = null;
		if (preblock instanceof CropBlock) {
			compareitem = ((CropBlock)preblock).getCloneItemStack(world, ipos, null).getItem();
		}
		
		ItemEntity itementity = (ItemEntity)entity;
		ItemStack itemstack = itementity.getItem();
		Item item = itemstack.getItem();

		if (item.equals(compareitem)) {
			world.setBlockAndUpdate(ipos, preblock.defaultBlockState());
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
			entity.remove(RemovalReason.DISCARDED);
		}
	}
}
