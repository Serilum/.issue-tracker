/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.17.x, mod version: 3.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Advancement Screenshot ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
