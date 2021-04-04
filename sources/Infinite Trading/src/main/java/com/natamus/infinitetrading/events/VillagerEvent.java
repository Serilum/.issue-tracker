/*
 * This is the latest source code of Infinite Trading.
 * Minecraft version: 1.16.5, mod version: 1.4.
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

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillagerEvent {
	private static Field usesField = null;
	private static Field maxUsesField = null;
	
	@SubscribeEvent
	public void onVillagerClick(PlayerInteractEvent.EntityInteract e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		Entity target = e.getTarget();
		MerchantOffers offers = null;
		
		if (target instanceof VillagerEntity == false) {
			if (ConfigHandler.GENERAL.wanderingTraderInfiniteTrades.get()) {
				if (target instanceof WanderingTraderEntity) {
					WanderingTraderEntity wanderer = (WanderingTraderEntity)target;
					offers = wanderer.getOffers();
				}
			}
		}
		else {
			VillagerEntity villager = (VillagerEntity)target;
			offers = villager.getOffers();
		}
		
		if (offers == null) {
			return;
		}
		
		if (usesField == null || maxUsesField == null) {
			for (Field field : MerchantOffer.class.getDeclaredFields()) {
				if (field.toString().contains("uses") || field.toString().contains("field_222226_d")) {
					usesField = field;
				}
				if (field.toString().contains("maxUses") || field.toString().contains("field_222227_e")) {
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
