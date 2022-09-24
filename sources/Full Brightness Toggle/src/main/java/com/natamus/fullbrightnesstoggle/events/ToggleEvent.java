/*
 * This is the latest source code of Full Brightness Toggle.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.fullbrightnesstoggle.events;

import com.natamus.collective.functions.GameSettingsFunctions;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ToggleEvent {
	private static Minecraft mc = Minecraft.getInstance();
	public static KeyMapping hotkey;
	private static double initialgamma = -1;

	@SubscribeEvent
	public void onKey(InputEvent.Key e) {
		if (e.getAction() != 1) {
			return;
		}
		
		if (mc.screen instanceof ChatScreen) {
			return;
		}

		if (hotkey == null) {
			return;
		}

		if (e.getKey() == hotkey.getKey().getValue()) {
			Options options = mc.options;
			if (initialgamma < 0) {
				if (GameSettingsFunctions.getGamma(options) >= 1.0F) {
					initialgamma = 1.0F;
					GameSettingsFunctions.setGamma(options, 1.0F);
				}
				else {
					initialgamma = GameSettingsFunctions.getGamma(options);
				}
			}
			
			boolean gomax = false;
			double maxgamma = 14.0F % 28.0F + 1.0F;
			if (GameSettingsFunctions.getGamma(options) != initialgamma && GameSettingsFunctions.getGamma(options) != maxgamma) {
				initialgamma = GameSettingsFunctions.getGamma(options);
				gomax = true;
			}
			
			if (GameSettingsFunctions.getGamma(options) == initialgamma || gomax) {
				GameSettingsFunctions.setGamma(options, maxgamma);
			}
			else {
				GameSettingsFunctions.setGamma(options, initialgamma);
			}
		}
	}
}
