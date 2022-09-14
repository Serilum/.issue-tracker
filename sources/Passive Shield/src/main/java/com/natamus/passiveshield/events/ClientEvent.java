/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.19.2, mod version: 2.6.
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
