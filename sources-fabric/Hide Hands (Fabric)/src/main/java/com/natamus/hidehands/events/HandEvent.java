/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.19.x, mod version: 1.6.
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

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.hidehands.config.ConfigHandler;

import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class HandEvent {
	public static boolean onHandRender(InteractionHand hand, PoseStack poseStack, ItemStack itemstack) {
		if (hand.equals(InteractionHand.MAIN_HAND)) {
			if (!ConfigHandler.alwaysHideMainHand.getValue()) {
				if(!isHoldingItem(ConfigHandler.hideMainHandWithItems.getValue(), itemstack)) {
					return true;
				}
			}
		}
		else if (hand.equals(InteractionHand.OFF_HAND)) {
			if (!ConfigHandler.alwaysHideOffhand.getValue()) {
				if(!isHoldingItem(ConfigHandler.hideOffhandWithItems.getValue(), itemstack)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private static boolean isHoldingItem(String hideitems, ItemStack item) {
		if (hideitems.length() > 1) {
			String itemstackid = Registry.ITEM.getKey(item.getItem()).toString().toLowerCase();
			if (hideitems.toLowerCase().contains(itemstackid)) {
				return true;
			}
		}
		
		return false;
	}
}
