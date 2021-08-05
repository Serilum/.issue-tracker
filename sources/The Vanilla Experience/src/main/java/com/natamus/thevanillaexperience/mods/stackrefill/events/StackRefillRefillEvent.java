/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.stackrefill.events;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class StackRefillRefillEvent {	
	private static List<Pair<PlayerEntity, ItemStack>> addstack = new ArrayList<Pair<PlayerEntity, ItemStack>>();
	private static List<Pair<Hand, Pair<PlayerEntity, ItemStack>>> addsingle = new ArrayList<Pair<Hand, Pair<PlayerEntity, ItemStack>>>();
	
	private static List<Pair<PlayerEntity, Hand>> checkfishingrod = new ArrayList<Pair<PlayerEntity, Hand>>();
	private static List<Pair<Hand, Pair<PlayerEntity, Item>>> checkitemused = new ArrayList<Pair<Hand, Pair<PlayerEntity, Item>>>();
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		World world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (addstack.size() > 0) {
			Pair<PlayerEntity, ItemStack> addstackcheck = addstack.get(0);
			PlayerEntity player = addstackcheck.getFirst();
			ItemStack togive = addstackcheck.getSecond();
			
			ItemStack heldmainhand = player.getMainHandItem();
			if (heldmainhand.isEmpty()) {
				player.setItemInHand(Hand.MAIN_HAND, togive);
			}
			else {
				ItemFunctions.giveOrDropItemStack(player, togive);
			}
			
			player.inventoryMenu.broadcastChanges();
			addstack.remove(0);
		}
		if (addsingle.size() > 0) {
			Pair<Hand, Pair<PlayerEntity, ItemStack>> pair = addsingle.get(0);
			Pair<PlayerEntity, ItemStack> insidepair = pair.getSecond();
			
			Hand hand = pair.getFirst();
			PlayerEntity player = insidepair.getFirst();
			player.setItemInHand(hand, insidepair.getSecond());
			
			player.inventoryMenu.broadcastChanges();
			addsingle.remove(0);
		}
		if (checkfishingrod.size() > 0) {
			Pair<PlayerEntity, Hand> pair = checkfishingrod.get(0);
			
			PlayerEntity player = pair.getFirst();
			Hand hand = pair.getSecond();
			if (player.getItemInHand(hand).isEmpty()) {
				PlayerInventory inv = player.inventory;
				
				for (int i=35; i > 8; i--) {
					ItemStack slot = inv.getItem(i);
					if (slot.getItem() instanceof FishingRodItem) {
						player.setItemInHand(hand, slot.copy());
						slot.setCount(0);
						break;
					}
				}
			}
			
			player.inventoryMenu.broadcastChanges();
			checkfishingrod.remove(0);
		}
		if (checkitemused.size() > 0) { 
			Pair<Hand, Pair<PlayerEntity, Item>> pair = checkitemused.get(0);
			Pair<PlayerEntity, Item> insidepair = pair.getSecond();
			
			Hand hand = pair.getFirst();
			PlayerEntity player = insidepair.getFirst();
			if (player.getItemInHand(hand).isEmpty()) {
				Item useditem = insidepair.getSecond();
				PlayerInventory inv = player.inventory;
				
				for (int i=35; i > 8; i--) {
					ItemStack slot = inv.getItem(i);
					if (useditem.equals(slot.getItem())) {
						player.setItemInHand(hand, slot.copy());
						slot.setCount(0);
						break;
					}
				}
				
				player.inventoryMenu.broadcastChanges();
			}
			
			checkitemused.remove(0);
		}
	}
	
	@SubscribeEvent
	public void onBlockPlace(BlockEvent.EntityPlaceEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)entity;
		if (player.isCreative()) {
			return;
		}
		
		Hand activehand = Hand.MAIN_HAND;
		ItemStack active = player.getMainHandItem();
		if (active.getItem() instanceof BlockItem == false) {
			active = player.getOffhandItem();
			if (active.getItem() instanceof BlockItem == false) {
				return;
			}
			
			activehand = Hand.OFF_HAND;
		}
		
		int amount = active.getCount();
		if (amount > 1) {
			return;
		}
		
		Item activeitem = active.getItem();
		PlayerInventory inv = player.inventory;
		
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			if (activeitem.equals(slot.getItem())) {
				int slotcount = slot.getCount();
				if (slotcount < 2) {
					Pair<PlayerEntity, ItemStack> toadd = new Pair<PlayerEntity, ItemStack>(player, slot.copy());
					addstack.add(toadd);
				}
				else {
					player.setItemInHand(activehand, slot.copy());
				}

				slot.setCount(0);
				break;
			}
		}
		
		player.inventoryMenu.broadcastChanges();
	}
	
	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Finish e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)entity;
		if (player.isCreative()) {
			return;
		}
		
		ItemStack used = e.getItem();
		
		int amount = used.getCount();
		if (amount > 1) {
			return;
		}
		
		Item useditem = used.getItem();

		PlayerInventory inv = player.inventory;
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

				e.setResultStack(slot.copy());
				slot.setCount(0);
				break;
			}
		}
		
		player.inventoryMenu.broadcastChanges();
	}
	
	@SubscribeEvent
	public void onItemBreak(PlayerDestroyItemEvent e) {
		PlayerEntity player = e.getPlayer();
		World world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (player.isCreative()) {
			return;
		}
		
		ItemStack used = e.getOriginal();
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
		
		Hand hand = e.getHand();
		if (hand == null) {
			return;
		}

		PlayerInventory inv = player.inventory;
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			Item slotitem = slot.getItem();
			if (useditem.equals(slotitem)) {
				Pair<PlayerEntity, ItemStack> insidepair = new Pair<PlayerEntity, ItemStack>(player, slot.copy());
				Pair<Hand, Pair<PlayerEntity, ItemStack>> pair = new Pair<Hand, Pair<PlayerEntity, ItemStack>>(hand, insidepair);
				addsingle.add(pair);
				slot.setCount(0);
				break;
			}
		}
		
		player.inventoryMenu.broadcastChanges();
	}
	
	@SubscribeEvent
	public void onItemToss(ItemTossEvent e) {
		PlayerEntity player = e.getPlayer();
		World world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (player.isCreative()) {
			return;
		}
		
		ItemStack tossed = e.getEntityItem().getItem();
		Item tosseditem = tossed.getItem();
		
		Hand activehand = Hand.MAIN_HAND;
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
			
			activehand = Hand.OFF_HAND;
		}
		
		if (active.getCount() > 1) {
			return;
		}
		
		PlayerInventory inv = player.inventory;
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			if (tosseditem.equals(slot.getItem())) {
				int slotcount = slot.getCount();
				if (slotcount < 2) {
					Pair<PlayerEntity, ItemStack> toadd = new Pair<PlayerEntity, ItemStack>(player, slot.copy());
					addstack.add(toadd);
				}
				else {
					player.setItemInHand(activehand, slot.copy());
				}

				slot.setCount(0);
				break;
			}
		}
		
		player.inventoryMenu.broadcastChanges();
	}
	
	@SubscribeEvent
	public void onItemBreak(PlayerInteractEvent.RightClickItem e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack stack = e.getItemStack();
		Item item = stack.getItem();
		if (item instanceof FishingRodItem) {
			int damage = stack.getDamageValue();
			int maxdamage = stack.getMaxDamage();
			
			if (maxdamage - damage < 5) {
				PlayerEntity player = e.getPlayer();
				Hand hand = e.getHand();
				
				Pair<PlayerEntity, Hand> toadd = new Pair<PlayerEntity, Hand>(player, hand);
				checkfishingrod.add(toadd);
			}
		}
		else if (item instanceof EggItem) {
			PlayerEntity player = e.getPlayer();
			Hand hand = e.getHand();
			
			Pair<PlayerEntity, Item> insidepair = new Pair<PlayerEntity, Item>(player, stack.getItem());
			Pair<Hand, Pair<PlayerEntity, Item>> pair = new Pair<Hand, Pair<PlayerEntity, Item>>(hand, insidepair);
			checkitemused.add(pair);		
		}
	}
	
	@SubscribeEvent
	public void onBlockRightClick(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)entity;
		if (player.isCreative()) {
			return;
		}
		
		Hand activehand = e.getHand();
		ItemStack active = player.getMainHandItem();
		
		int amount = active.getCount();
		if (amount > 26) {
			return;
		}
		
		Pair<PlayerEntity, Item> insidepair = new Pair<PlayerEntity, Item>(player, active.getItem());
		Pair<Hand, Pair<PlayerEntity, Item>> pair = new Pair<Hand, Pair<PlayerEntity, Item>>(activehand, insidepair);
		checkitemused.add(pair);
	}
}
