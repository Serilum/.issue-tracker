/*
 * This is the latest source code of Stack Refill.
 * Minecraft version: 1.19.2, mod version: 3.0.
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
import com.natamus.collective.functions.ItemFunctions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EventBusSubscriber
public class RefillEvent {	
	private static List<Pair<Player, ItemStack>> addstack = new ArrayList<Pair<Player, ItemStack>>();
	private static List<Pair<InteractionHand, Pair<Player, ItemStack>>> addsingle = new ArrayList<Pair<InteractionHand, Pair<Player, ItemStack>>>();
	
	private static List<Pair<Player, InteractionHand>> checkfishingrod = new ArrayList<Pair<Player, InteractionHand>>();
	private static List<Pair<InteractionHand, Pair<Player, ItemStack>>> checkitemused = new ArrayList<Pair<InteractionHand, Pair<Player, ItemStack>>>();
	private static HashMap<String, InteractionHand> lasthandused = new HashMap<String, InteractionHand>();

	@SubscribeEvent
	public void onWorldTick(TickEvent.ServerTickEvent e) {
		if (!e.phase.equals(Phase.START)) {
			return;
		}

		processTick(false);
	}

	public static void processTick(boolean isClientSide) {
		try {
			if (addstack.size() > 0) {
				Pair<Player, ItemStack> addstackcheck = addstack.get(0);
				Player player = addstackcheck.getFirst();
				ItemStack togive = addstackcheck.getSecond();

				ItemStack heldmainhand = player.getMainHandItem();
				if (heldmainhand.isEmpty()) {
					player.setItemInHand(InteractionHand.MAIN_HAND, togive);
				} else {
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
				ItemStack handstack = player.getItemInHand(hand).copy();

				player.setItemInHand(hand, insidepair.getSecond());

				if (!handstack.isEmpty()) {
					ItemFunctions.giveOrDropItemStack(player, handstack);
				}

				player.getInventory().setChanged();
				addsingle.remove(0);
			}
			if (checkfishingrod.size() > 0) {
				Pair<Player, InteractionHand> pair = checkfishingrod.get(0);

				Player player = pair.getFirst();
				InteractionHand hand = pair.getSecond();
				if (player.getItemInHand(hand).isEmpty()) {
					Inventory inv = player.getInventory();

					for (int i = 35; i > 8; i--) {
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
				Pair<InteractionHand, Pair<Player, ItemStack>> pair = checkitemused.get(0);
				Pair<Player, ItemStack> insidepair = pair.getSecond();

				InteractionHand hand = pair.getFirst();
				Player player = insidepair.getFirst();
				if (!player.isUsingItem()) {
					ItemStack usedstack = insidepair.getSecond();
					ItemStack handstack = player.getItemInHand(hand).copy();
					if (!(usedstack.getItem().equals(handstack.getItem()) && usedstack.getCount() == handstack.getCount())) {
						boolean shouldcontinue = false;
						if (handstack.getCount() <= 1) {
							if (usedstack.getItem().equals(handstack.getItem())) {
								if (handstack.isEmpty()) {
									shouldcontinue = true;
								}
							}
							else {
								shouldcontinue = true;
							}
						}

						if (shouldcontinue) {
							Item useditem = usedstack.getItem();

							Inventory inv = player.getInventory();
							for (int i = 35; i > 8; i--) {
								ItemStack slot = inv.getItem(i);
								Item slotitem = slot.getItem();
								if (useditem.equals(slotitem)) {
									if (slotitem instanceof PotionItem) {
										if (!PotionUtils.getPotion(usedstack).equals(PotionUtils.getPotion(slot))) {
											continue;
										}
									}

									player.setItemInHand(hand, slot.copy());
									slot.setCount(0);

									if (!handstack.isEmpty()) {
										ItemFunctions.giveOrDropItemStack(player, handstack);
									}

									player.getInventory().setChanged();
									break;
								}
							}
						}
					}
				}

				checkitemused.remove(0);
			}
		}
		catch(IndexOutOfBoundsException ignored) {}
	}

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Start e) {
		Entity entity = e.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}

		Player player = (Player)entity;
		ItemStack mainstack = player.getMainHandItem();
		ItemStack used = e.getItem();

		InteractionHand hand = InteractionHand.MAIN_HAND;
		if (!(mainstack.getItem().equals(used.getItem()) && mainstack.getCount() == used.getCount())) {
			hand = InteractionHand.OFF_HAND;
		}

		lasthandused.put(player.getName().getString(), hand);
	}

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Finish e) {
		Entity entity = e.getEntity();
		if (!(entity instanceof Player)) {
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

		String playername = player.getName().getString();
		if (!lasthandused.containsKey(playername)) {
			return;
		}

		InteractionHand hand = lasthandused.get(playername);

		Pair<Player, ItemStack> insidepair = new Pair<Player, ItemStack>(player, used.copy());
		Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<InteractionHand, Pair<Player, ItemStack>>(hand, insidepair);
		checkitemused.add(pair);
	}
	
	@SubscribeEvent
	public void onItemBreak(PlayerDestroyItemEvent e) {
		Player player = e.getEntity();
		if (player.isCreative()) {
			return;
		}
		
		ItemStack used = e.getOriginal();
		if (used == null) {
			return;
		}
		
		Item useditem = used.getItem();
		if (useditem instanceof BlockItem || useditem instanceof BucketItem || useditem instanceof PotionItem) {
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
		if (player.isCreative()) {
			return;
		}

		ItemStack tossedstack = e.getEntity().getItem();
		Item tosseditem = tossedstack.getItem();
		
		InteractionHand activehand = InteractionHand.MAIN_HAND;
		ItemStack activestack = player.getMainHandItem();

		if (!activestack.isEmpty()) {
			return;
		}

		if (tossedstack.getCount() > 1) {
			return;
		}
		
		Inventory inv = player.getInventory();
		for (int i=35; i > 8; i--) {
			ItemStack slot = inv.getItem(i);
			Item slotitem = slot.getItem();
			if (tosseditem.equals(slotitem)) {
				if (slotitem instanceof PotionItem) {
					if (!PotionUtils.getPotion(tossedstack).equals(PotionUtils.getPotion(slot))) {
						continue;
					}
				}

				player.setItemInHand(activehand, slot.copy());
				slot.setCount(0);
				break;
			}
		}
		
		player.getInventory().setChanged();
	}
	
	@SubscribeEvent
	public void onItemBreak(PlayerInteractEvent.RightClickItem e) {
		Player player = e.getEntity();
		if (player.isCreative()) {
			return;
		}
		
		ItemStack stack = e.getItemStack();
		Item item = stack.getItem();
		if (item instanceof FishingRodItem) {
			int damage = stack.getDamageValue();
			int maxdamage = stack.getMaxDamage();
			
			if (maxdamage - damage < 5) {
				InteractionHand hand = e.getHand();
				
				Pair<Player, InteractionHand> toadd = new Pair<Player, InteractionHand>(player, hand);
				checkfishingrod.add(toadd);
			}
		}
		else if (item instanceof EggItem || item instanceof SnowballItem || item instanceof FireworkRocketItem) {
			if (stack.getCount() > 1) {
				return;
			}

			InteractionHand hand = e.getHand();
			
			Pair<Player, ItemStack> insidepair = new Pair<Player, ItemStack>(player, stack.copy());
			Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<InteractionHand, Pair<Player, ItemStack>>(hand, insidepair);
			checkitemused.add(pair);		
		}
	}
	
	@SubscribeEvent
	public void onBlockRightClick(PlayerInteractEvent.RightClickBlock e) {
		Player player = e.getEntity();
		if (player.isCreative()) {
			return;
		}

		if (player.isUsingItem()) {
			return;
		}

		InteractionHand activehand = e.getHand();
		ItemStack active = player.getItemInHand(activehand);
		
		int amount = active.getCount();
		if (amount > 26) {
			return;
		}
		
		Pair<Player, ItemStack> insidepair = new Pair<Player, ItemStack>(player, active.copy());
		Pair<InteractionHand, Pair<Player, ItemStack>> pair = new Pair<InteractionHand, Pair<Player, ItemStack>>(activehand, insidepair);
		checkitemused.add(pair);
	}
}
