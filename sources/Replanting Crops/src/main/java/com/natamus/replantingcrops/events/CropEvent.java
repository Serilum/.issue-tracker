/*
 * This is the latest source code of Replanting Crops.
 * Minecraft version: 1.17.1, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Replanting Crops ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.replantingcrops.events;

import java.util.HashMap;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.replantingcrops.config.ConfigHandler;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CropEvent {
	private static HashMap<BlockPos, Item> checkreplant = new HashMap<BlockPos, Item>();
	private static HashMap<BlockPos, BlockState> cocoaStates = new HashMap<BlockPos, BlockState>();
	
	@SubscribeEvent
	public void onHarvest(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Player player = e.getPlayer();
		if (player == null) {
			return;
		}
		
		if (ConfigHandler.GENERAL.mustHoldHoeForReplanting.get()) {
			if (player.getMainHandItem().getItem() instanceof HoeItem == false) {
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
	}
	
	@SubscribeEvent
	public void onHarvest(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
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
