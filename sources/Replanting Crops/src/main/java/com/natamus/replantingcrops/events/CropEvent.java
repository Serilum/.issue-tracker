/*
 * This is the latest source code of Replanting Crops.
 * Minecraft version: 1.19.2, mod version: 4.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.replantingcrops.events;

import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.ToolFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.replantingcrops.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;

@EventBusSubscriber
public class CropEvent {
	private static HashMap<BlockPos, Block> checkreplant = new HashMap<BlockPos, Block>();
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
			if (!ToolFunctions.isHoe(player.getMainHandItem())) {
				hand = InteractionHand.OFF_HAND;
				if (!ToolFunctions.isHoe(player.getOffhandItem())) {
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
			return;
		}
		
		if (!player.isCreative()) {
			ItemFunctions.itemHurtBreakAndEvent(player.getItemInHand(hand), (ServerPlayer)player, hand, 1);
		}
	}
	
	@SubscribeEvent
	public void onHarvest(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
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
			e.setCanceled(true);
		}
	}
}
