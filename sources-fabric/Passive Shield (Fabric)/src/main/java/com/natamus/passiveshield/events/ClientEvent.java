/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.19.2, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.passiveshield.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.passiveshield.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class ClientEvent {
	private static final Minecraft mc = Minecraft.getInstance();

	public static boolean onHandRender(InteractionHand hand, PoseStack poseStack, ItemStack itemStack) {
		if (hand.equals(InteractionHand.OFF_HAND)) {
			if (ConfigHandler.hideShieldWhenNotInUse.getValue()) {
				if (itemStack.getItem() instanceof ShieldItem) {
					ItemStack useItem = mc.player.getUseItem();
					if (mc.player.isUsingItem() && useItem.getItem() instanceof ShieldItem) {
						return true;
					}

					return false;
				}
			}
		}

		return true;
	}
}
