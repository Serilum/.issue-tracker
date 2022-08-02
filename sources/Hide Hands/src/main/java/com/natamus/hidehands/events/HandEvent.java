/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.19.1, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.hidehands.events;

import com.natamus.hidehands.config.ConfigHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber
public class HandEvent {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onHandRender(RenderHandEvent e) {
		InteractionHand hand = e.getHand();
		ItemStack itemstack = e.getItemStack();
		
		if (hand.equals(InteractionHand.MAIN_HAND)) {
			if (!ConfigHandler.GENERAL.alwaysHideMainHand.get()) {
				if(!isHoldingItem(ConfigHandler.GENERAL.hideMainHandWithItems.get(), itemstack)) {
					return;
				}
			}
		}
		else if (hand.equals(InteractionHand.OFF_HAND)) {
			if (!ConfigHandler.GENERAL.alwaysHideOffhand.get()) {
				if(!isHoldingItem(ConfigHandler.GENERAL.hideOffhandWithItems.get(), itemstack)) {
					return;
				}
			}
		}
		
		e.setCanceled(true);
	}
	
	private static boolean isHoldingItem(String hideitems, ItemStack item) {
		if (hideitems.length() > 1) {
			String itemstackid = ForgeRegistries.ITEMS.getKey(item.getItem()).toString().toLowerCase();
			if (hideitems.toLowerCase().contains(itemstackid)) {
				return true;
			}
		}
		
		return false;
	}
}
