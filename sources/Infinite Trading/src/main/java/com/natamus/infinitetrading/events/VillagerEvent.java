/*
 * This is the latest source code of Infinite Trading.
 * Minecraft version: 1.19.2, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

@EventBusSubscriber
public class VillagerEvent {
	private static Field usesField = ObfuscationReflectionHelper.findField(MerchantOffer.class, "f_45313_"); // uses
	private static Field maxUsesField = ObfuscationReflectionHelper.findField(MerchantOffer.class, "f_45314_"); // maxUses
	
	@SubscribeEvent
	public void onVillagerClick(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getLevel();
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
