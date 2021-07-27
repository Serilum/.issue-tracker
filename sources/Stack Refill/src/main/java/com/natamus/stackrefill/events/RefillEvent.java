/*
 * This is the latest source code of Stack Refill.
 * Minecraft version: 1.17.1, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Stack Refill ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.stackrefill.events;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
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
public class RefillEvent {	
	private static List<Pair<Player, ItemStack>> addstack = new ArrayList<Pair<Player, ItemStack>>();
	private static List<Pair<InteractionHand, Pair<Player, ItemStack>>> addsingle = new ArrayList<Pair<InteractionHand, Pair<Player, ItemStack>>>();
	
	private static List<Pair<Player, InteractionHand>> checkfishingrod = new ArrayList<Pair<Player, InteractionHand>>();
	private static List<Pair<InteractionHand, Pair<Player, Item>>> checkitemused = new ArrayList<Pair<InteractionHand, Pair<Player, Item>>>();
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
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
	
	@SubscribeEvent
	public void onBlockPlace(BlockEvent.EntityPlaceEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Player == false) {
			return;
		}
		
		Player player = (Player)entity;
		if (player.isCreative()) {
			return;
		}
		
		InteractionHand activehand = InteractionHand.MAIN_HAND;
		ItemStack active = player.getMainHandItem();
		if (active.getItem() instanceof BlockItem == false) {
			active = player.getOffhandItem();
			if (active.getItem() instanceof BlockItem == false) {
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
	
	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Finish e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof Player == false) {
			return;
		}
		
		Player player = (Player)entity;
		if (player.isCreative()) {
			return;
		}
		
		ItemStack used = e.getItem();
		
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

				e.setResultStack(slot.copy());
				slot.setCount(0);
				break;
			}
		}
		
		player.getInventory().setChanged();
	}
	
	@SubscribeEvent
	public void onItemBreak(PlayerDestroyItemEvent e) {
		Player player = e.getPlayer();
		Level world = player.getCommandSenderWorld();
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
		
		InteractionHand hand = e.getHand();
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
	
	@SubscribeEvent
	public void onItemToss(ItemTossEvent e) {
		Player player = e.getPlayer();
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (player.isCreative()) {
			return;
		}
		
		ItemStack tossed = e.getEntityItem().getItem();
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
	
	@SubscribeEvent
	public void onItemBreak(PlayerInteractEvent.RightClickItem e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack stack = e.getItemStack();
		Item item = stack.getItem();
		if (item instanceof FishingRodItem) {
			int damage = stack.getDamageValue();
			int maxdamage = stack.getMaxDamage();
			
			if (maxdamage - damage < 5) {
				Player player = e.getPlayer();
				InteractionHand hand = e.getHand();
				
				Pair<Player, InteractionHand> toadd = new Pair<Player, InteractionHand>(player, hand);
				checkfishingrod.add(toadd);
			}
		}
		else if (item instanceof EggItem) {
			Player player = e.getPlayer();
			InteractionHand hand = e.getHand();
			
			Pair<Player, Item> insidepair = new Pair<Player, Item>(player, stack.getItem());
			Pair<InteractionHand, Pair<Player, Item>> pair = new Pair<InteractionHand, Pair<Player, Item>>(hand, insidepair);
			checkitemused.add(pair);		
		}
	}
	
	@SubscribeEvent
	public void onBlockRightClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Player == false) {
			return;
		}
		
		Player player = (Player)entity;
		if (player.isCreative()) {
			return;
		}
		
		InteractionHand activehand = e.getHand();
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
