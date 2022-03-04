/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.18.2, mod version: 2.0.
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

import com.natamus.passiveshield.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvent {
	private final Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent
	public void onHandRender(RenderHandEvent e) {
		InteractionHand hand = e.getHand();
		ItemStack itemstack = e.getItemStack();

		if (hand.equals(InteractionHand.OFF_HAND)) {
			if (ConfigHandler.GENERAL.hideShieldWhenNotInUse.get()) {
				if (itemstack.getItem() instanceof ShieldItem) {
					ItemStack useItem = mc.player.getUseItem();
					if (mc.player.isUsingItem() && useItem.getItem() instanceof ShieldItem) {
						return;
					}

					e.setCanceled(true);
				}
			}
		}
	}
}
