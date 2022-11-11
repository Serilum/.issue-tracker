/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.19.2, mod version: 3.0.
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

import com.natamus.hidehands.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(value = Dist.CLIENT)
public class HandEvent {
	private static final Minecraft mc = Minecraft.getInstance();
	private static int hideDelay = 0;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onHandRender(RenderHandEvent e) {
		InteractionHand hand = e.getHand();
		ItemStack itemstack = e.getItemStack();

		if (hand.equals(InteractionHand.MAIN_HAND)) {
			if (!itemstack.isEmpty()) {
				if (!ConfigHandler.GENERAL.alwaysHideMainHand.get()) {
					if (!isHoldingItem(ConfigHandler.GENERAL.hideMainHandWithItems.get(), itemstack)) {
						hideDelay = 10;
						return;
					}
				}
			}
			else if (!ConfigHandler.GENERAL.alwaysHideEmptyMainHand.get()) {
				if (hideDelay > 0) {
					hideDelay -= 1;
				}
				return;
			}
			else if (mc.player.swinging) {
				hideDelay = 30;
			}

			if (hideDelay > 0) {
				hideDelay -= 1;
				return;
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
			String itemstackid = ForgeRegistries.ITEMS.getKey(item.getItem()).toString().toLowerCase();
			return hideitems.toLowerCase().contains(itemstackid);
		}
		return false;
	}
}