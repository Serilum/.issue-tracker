/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.19.3, mod version: 3.1.
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

package com.natamus.hidehands.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.hidehands.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class HandEvent {
	private static final Minecraft mc = Minecraft.getInstance();
	private static int hideDelay = 0;

	public static boolean onHandRender(InteractionHand hand, PoseStack poseStack, ItemStack itemstack) {
		if (hand.equals(InteractionHand.MAIN_HAND)) {
			if (!itemstack.isEmpty()) {
				if (!ConfigHandler.alwaysHideMainHand) {
					if (!isHoldingItem(ConfigHandler.hideMainHandWithItems, itemstack)) {
						hideDelay = 10;
						return true;
					}
				}
			}
			else if (!ConfigHandler.alwaysHideEmptyMainHand) {
				if (hideDelay > 0) {
					hideDelay -= 1;
				}
				return true;
			}
			else if (mc.player.swinging) {
				hideDelay = 30;
			}

			if (hideDelay > 0) {
				hideDelay -= 1;
				return true;
			}
		}
		else if (hand.equals(InteractionHand.OFF_HAND)) {
			if (!ConfigHandler.alwaysHideOffhand) {
				return !isHoldingItem(ConfigHandler.hideOffhandWithItems, itemstack);
			}
		}

		return false;
	}
	
	private static boolean isHoldingItem(String hideitems, ItemStack item) {
		if (hideitems.length() > 1) {
			String itemstackid = BuiltInRegistries.ITEM.getKey(item.getItem()).toString().toLowerCase();
			return hideitems.toLowerCase().contains(itemstackid);
		}
		return false;
	}
}