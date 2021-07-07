/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.hidehands.events;

import com.natamus.thevanillaexperience.mods.hidehands.config.HideHandsConfigHandler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class HideHandsHandEvent {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onHandRender(RenderHandEvent e) {
		Hand hand = e.getHand();
		ItemStack itemstack = e.getItemStack();
		
		if (hand.equals(Hand.MAIN_HAND)) {
			if (!HideHandsConfigHandler.GENERAL.alwaysHideMainHand.get()) {
				if(!isHoldingItem(HideHandsConfigHandler.GENERAL.hideMainHandWithItems.get(), itemstack)) {
					return;
				}
			}
		}
		else if (hand.equals(Hand.OFF_HAND)) {
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
