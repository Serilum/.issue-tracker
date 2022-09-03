/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
