/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.19.2, mod version: 3.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.advancementscreenshot.events;

import com.natamus.advancementscreenshot.config.ConfigHandler;
import com.natamus.advancementscreenshot.util.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AdvancementGetEvent {
	private final Minecraft mc = Minecraft.getInstance();
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) {
		if (!e.phase.equals(Phase.END)) {
			return;
		}
		
		if (Util.cooldown > 0) {
			Util.cooldown -= 1;
			return;
		}
		
		if (Util.takescreenshot) {
			if (Util.cooldown < 0) {
				Util.cooldown = 20;
				return;
			}
			
			Screenshot.grab(mc.gameDirectory, mc.getMainRenderTarget(), (context) -> {
				mc.execute(() -> {
					if (ConfigHandler.GENERAL.showScreenshotTakenMessage.get()) {
						mc.gui.getChat().addMessage(context);
					}
				});
			});
			
			Util.takescreenshot = false;
			Util.cooldown = -1;
		}
	}
}
