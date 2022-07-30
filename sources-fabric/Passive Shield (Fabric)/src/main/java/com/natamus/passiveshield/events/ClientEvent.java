/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.19.1, mod version: 2.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Passive Shield ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
