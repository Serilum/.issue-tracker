/*
 * This is the latest source code of Infinite Trading.
 * Minecraft version: 1.17.1, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Infinite Trading ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.infinitetrading.events;

import java.lang.reflect.Field;

import com.natamus.infinitetrading.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillagerEvent {
	private static Field usesField = null;
	private static Field maxUsesField = null;
	
	@SubscribeEvent
	public void onVillagerClick(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity target = e.getTarget();
		MerchantOffers offers = null;
		
		if (target instanceof Villager == false) {
			if (ConfigHandler.GENERAL.wanderingTraderInfiniteTrades.get()) {
				if (target instanceof WanderingTrader) {
					WanderingTrader wanderer = (WanderingTrader)target;
					offers = wanderer.getOffers();
				}
			}
		}
		else {
			Villager villager = (Villager)target;
			offers = villager.getOffers();
		}
		
		if (offers == null) {
			return;
		}
		
		if (usesField == null || maxUsesField == null) {
			for (Field field : MerchantOffer.class.getDeclaredFields()) {
				if (field.toString().contains("uses") || field.toString().contains("f_45313_")) {
					usesField = field;
				}
				if (field.toString().contains("maxUses") || field.toString().contains("f_45314_")) {
					maxUsesField = field;
				}
			}
			if (usesField == null || maxUsesField == null) {
				return;
			}
			usesField.setAccessible(true);
			maxUsesField.setAccessible(true);
		}
		
		for (MerchantOffer offer : offers) {
			try {
				usesField.set(offer, 0);
				maxUsesField.set(offer, 99999);
			} catch (Exception ex) {
				return;
			}
		}
	}
}
