/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.18.0, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Hide Hands ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.hidehands.events;

import com.natamus.hidehands.config.ConfigHandler;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

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
			String itemstackid = item.getItem().getRegistryName().toString().toLowerCase();
			if (hideitems.toLowerCase().contains(itemstackid)) {
				return true;
			}
		}
		
		return false;
	}
}
