/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.hidehands.events;

import com.natamus.thevanillaexperience.mods.hidehands.config.HideHandsConfigHandler;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class HideHandsHandEvent {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onHandRender(RenderHandEvent e) {
		InteractionHand hand = e.getHand();
		ItemStack itemstack = e.getItemStack();
		
		if (hand.equals(InteractionHand.MAIN_HAND)) {
			if (!HideHandsConfigHandler.GENERAL.alwaysHideMainHand.get()) {
				if(!isHoldingItem(HideHandsConfigHandler.GENERAL.hideMainHandWithItems.get(), itemstack)) {
					return;
				}
			}
		}
		else if (hand.equals(InteractionHand.OFF_HAND)) {
			if (!HideHandsConfigHandler.GENERAL.alwaysHideOffhand.get()) {
				if(!isHoldingItem(HideHandsConfigHandler.GENERAL.hideOffhandWithItems.get(), itemstack)) {
					return;
				}
			}
		}
		
		e.setCanceled(true);
	}
	
	private static boolean isHoldingItem(String hideitems, ItemStack item) {
		if (hideitems.length() > 1) {
			String itemstackid = item.getItem().getRegistryName().toString().toLowerCase();
			if (hideitems.toLowerCase().contains(itemstackid)) {
				return true;
			}
		}
		
		return false;
	}
}
