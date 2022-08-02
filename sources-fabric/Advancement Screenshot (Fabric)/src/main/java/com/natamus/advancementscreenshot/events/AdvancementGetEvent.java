/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.19.1, mod version: 3.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.advancementscreenshot.events;

import com.natamus.advancementscreenshot.config.ConfigHandler;
import com.natamus.advancementscreenshot.util.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

public class AdvancementGetEvent {
	public static void onClientTick(Minecraft mc) {
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
					if (ConfigHandler.showScreenshotTakenMessage.getValue()) {
						mc.gui.getChat().addMessage(context);
					}
				});
			});
			
			Util.takescreenshot = false;
			Util.cooldown = -1;
		}
	}
}
