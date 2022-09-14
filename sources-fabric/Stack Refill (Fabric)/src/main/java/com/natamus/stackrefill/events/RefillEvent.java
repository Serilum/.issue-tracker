/*
 * This is the latest source code of Stack Refill.
 * Minecraft version: 1.19.2, mod version: 2.6.
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

package com.natamus.stackrefill.events;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective_fabric.functions.ItemFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class RefillEvent {	
	private static List<Pair<Player, ItemStack>> addstack = new ArrayList<Pair<Player, ItemStack>>();
	private static List<Pair<InteractionHand, Pair<Player, ItemStack>>> addsingle = new ArrayList<Pair<InteractionHand, Pair<Player, ItemStack>>>();
	
	private static List<Pair<Player, InteractionHand>> checkfishingrod = new ArrayList<Pair<Player, InteractionHand>>();
	private static List<Pair<InteractionHand, Pair<Player, Item>>> checkitemused = new ArrayList<Pair<InteractionHand, Pair<Player, Item>>>();
	
	public static void onWorldTick(ServerLevel world) {
		if (addstack.size() > 0) {
			Pair<Player, ItemStack> addstackcheck = addstack.get(0);
			Player player = addstackcheck.getFirst();
			ItemStack togive = addstackcheck.getSecond();
			
			ItemStack heldmainhand = player.getMainHandItem();
			if (heldmainhand.isEmpty()) {
				player.setItemInHand(InteractionHand.MAIN_HAND, togive);
			}
			else {
				ItemFunctions.giveOrDropItemStack(player, togive);
			}
			
			player.getInventory().setChanged();
			addstack.remove(0);
		}
		if (addsingle.size() > 0) {
			Pair<InteractionHand, Pair<Player, ItemStack>> pair = addsingle.get(0);
			Pair<Player, ItemStack> insidepair = pair.getSecond();
			
			InteractionHand hand = pair.getFirst();
			Player player = insidepair.getFirst();
			player.setItemInHand(hand, insidepair.getSecond());
			
			player.getInventory().setChanged();
			addsingle.remove(0);
		}
		if (checkfishingrod.size() > 0) {
			Pair<Player, InteractionHand> pair = checkfishingrod.get(0);
			
			Player player = pair.getFirst();
			InteractionHand hand = pair.getSecond();
			if (player.getItemInHand(hand).isEmpty()) {
				Inventory inv = player.getInventory();
				
				for (int i=35; i > 8; i--) {
					ItemStack slot = inv.getItem(i);
					if (slot.getItem() instanceof FishingRodItem) {
						player.setItemInHand(hand, slot.copy());
						slot.setCount(0);
						break;
					}
				}
			}
			
			player.getInventory().setChanged();
			checkfishingrod.remove(0);
		}
		if (checkitemused.size() > 0) { 
			Pair<InteractionHand, Pair<Player, Item>> pair = checkitemused.get(0);
			Pair<Player, Item> insidepair = pair.getSecond();
			
			InteractionHand hand = pair.getFirst();
			Player player = insidepair.getFirst();
			if (player.getItemInHand(hand).isEmpty()) {
				Item useditem = insidepair.getSecond();
				Inventory inv = player.getInventory();
				
				for (int i=35; i > 8; i--) {
					ItemStack slot = inv.getItem(i);
					if (useditem.equals(slot.getItem())) {
						player.setItemInHand(hand, slot.copy());
						slot.setCount(0);
						break;
					}
				}
				
				player.getInventory().setChanged();
			}
			
			checkitemused.remove(0);
		}
	}
	
	public static void onBlockPlace(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
		if (level.isClientSide) {
			return;
		}

		if (!(livingEntity instanceof Player)) {
			return;
		}
		
		Player player = (Player)livingEntity;
		if (player.isCreative()) {
			return;
		}
		
		InteractionHand activehand = InteractionHand.MAIN_HAND;
		ItemStack active = player.getMainHandItem();
		if (!(active.getItem() instanceof BlockItem)) {
			active = player.getOffhandItem();
			if (!(active.getItem() instanceof BlockItem)) {
				return;
			}
			
			activehand = InteractionHand.OFF_HAND;
		}
		
		int amount = active.getCount();
		if (amount > 1) {
			return;
		}
		
		Item activeitem = active.getItem();
		Inventory inv = player.getInventory();
		
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			if (activeitem.equals(slot.getItem())) {
				int slotcount = slot.getCount();
				if (slotcount < 2) {
					Pair<Player, ItemStack> toadd = new Pair<Player, ItemStack>(player, slot.copy());
					addstack.add(toadd);
				}
				else {
					player.setItemInHand(activehand, slot.copy());
				}

				slot.setCount(0);
				break;
			}
		}
		
		player.getInventory().setChanged();
	}
	
	public static void onItemUse(Player player, ItemStack used, ItemStack newItem, InteractionHand hand) {
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}

		if (player.isCreative()) {
			return;
		}
		
		int amount = used.getCount();
		if (amount > 1) {
			return;
		}
		
		Item useditem = used.getItem();

		Inventory inv = player.getInventory();
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			Item slotitem = slot.getItem();
			if (useditem.equals(slotitem)) {
				if (slotitem instanceof PotionItem) {
					if (!PotionUtils.getPotion(used).equals(PotionUtils.getPotion(slot))) {
						continue;
					}
					ItemFunctions.giveOrDropItemStack(player, new ItemStack(Items.GLASS_BOTTLE, 1));
				}
				else if (slotitem instanceof MilkBucketItem) {
					ItemFunctions.giveOrDropItemStack(player, new ItemStack(Items.BUCKET, 1));
				}

				player.setItemInHand(hand, slot.copy());
				slot.setCount(0);
				break;
			}
		}
		
		player.getInventory().setChanged();
	}
	
	public static void onItemBreak(Player player, ItemStack used, InteractionHand hand) {
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (player.isCreative()) {
			return;
		}

		if (used == null) {
			return;
		}
		
		Item useditem = used.getItem();
		if (useditem instanceof BlockItem) {
			return;
		}
		
		if (useditem instanceof BucketItem) {
			return;
		}
		
		int amount = used.getCount();
		if (amount > 1) {
			return;
		}

		if (hand == null) {
			return;
		}

		Inventory inv = player.getInventory();
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			Item slotitem = slot.getItem();
			if (useditem.equals(slotitem)) {
				Pair<Player, ItemStack> insidepair = new Pair<Player, ItemStack>(player, slot.copy());
				Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<InteractionHand, Pair<Player, ItemStack>>(hand, insidepair);
				addsingle.add(pair);
				slot.setCount(0);
				break;
			}
		}
		
		player.getInventory().setChanged();
	}
	
	public static void onItemToss(Player player, ItemStack tossed) {
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (player.isCreative()) {
			return;
		}

		Item tosseditem = tossed.getItem();
		
		InteractionHand activehand = InteractionHand.MAIN_HAND;
		ItemStack active = player.getMainHandItem();
		if (!active.isEmpty()) {
			if (active.getItem().equals(tosseditem)) {
				return;
			}
			
			active = player.getOffhandItem();
			if (!active.isEmpty()) {
				return;
			}
			if (active.getItem().equals(tosseditem)) {
				return;
			}
			
			activehand = InteractionHand.OFF_HAND;
		}
		
		if (active.getCount() > 1) {
			return;
		}
		
		Inventory inv = player.getInventory();
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			if (tosseditem.equals(slot.getItem())) {
				int slotcount = slot.getCount();
				if (slotcount < 2) {
					Pair<Player, ItemStack> toadd = new Pair<Player, ItemStack>(player, slot.copy());
					addstack.add(toadd);
				}
				else {
					player.setItemInHand(activehand, slot.copy());
				}

				slot.setCount(0);
				break;
			}
		}
		
		player.getInventory().setChanged();
	}
	
	public static InteractionResultHolder<ItemStack> onItemRightClick(Player player, Level world, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (world.isClientSide) {
			return InteractionResultHolder.pass(stack);
		}

		if (player.isCreative()) {
			return InteractionResultHolder.pass(stack);
		}

		Item item = stack.getItem();
		if (item instanceof FishingRodItem) {
			int damage = stack.getDamageValue();
			int maxdamage = stack.getMaxDamage();
			
			if (maxdamage - damage < 5) {
				Pair<Player, InteractionHand> toadd = new Pair<Player, InteractionHand>(player, hand);
				checkfishingrod.add(toadd);
			}
		}
		else if (item instanceof EggItem) {
			Pair<Player, Item> insidepair = new Pair<Player, Item>(player, stack.getItem());
			Pair<InteractionHand, Pair<Player, Item>> pair = new Pair<InteractionHand, Pair<Player, Item>>(hand, insidepair);
			checkitemused.add(pair);		
		}

		return InteractionResultHolder.pass(stack);
	}
	
	public static void onBlockRightClick(Level world, Player player, InteractionHand activehand, BlockPos pos, BlockHitResult hitVec) {
		if (world.isClientSide) {
			return;
		}

		if (player.isCreative()) {
			return;
		}

		if (!activehand.equals(InteractionHand.MAIN_HAND)) {
			return;
		}
		
		ItemStack active = player.getMainHandItem();
		
		int amount = active.getCount();
		if (amount > 26) {
			return;
		}
		
		Pair<Player, Item> insidepair = new Pair<Player, Item>(player, active.getItem());
		Pair<InteractionHand, Pair<Player, Item>> pair = new Pair<InteractionHand, Pair<Player, Item>>(activehand, insidepair);
		checkitemused.add(pair);
	}
}
