/*
 * This is the latest source code of Full Brightness Toggle.
 * Minecraft version: 1.18.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Full Brightness Toggle ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.fullbrightnesstoggle.events;

import com.natamus.fullbrightnesstoggle.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ToggleEvent {
	private static Minecraft mc = null;
	private static double initialgamma = -1;
	private static double maxgamma = 14.0F % 28.0F + 1.0F;
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent e) {
		if (e.getAction() != 1) {
			return;
		}
		
		if (mc == null) {
			mc = Minecraft.getInstance();
		}
		
		if (mc.screen instanceof ChatScreen) {
			return;
		}
		
		if (e.getKey() == Main.hotkey.getKey().getValue()) {
			Options settings = mc.options;
			if (initialgamma < 0) {
				if (settings.gamma >= 1.0F) {
					initialgamma = 1.0F;
					settings.gamma = 1.0F;
				}
				else {
					initialgamma = settings.gamma;			
				}
			}
			
			boolean gomax = false;
			if (settings.gamma != initialgamma && settings.gamma != maxgamma) {
				initialgamma = settings.gamma;
				gomax = true;
			}
			
			if (settings.gamma == initialgamma || gomax) {
				settings.gamma = maxgamma;
			}
			else {		          
				settings.gamma = initialgamma;
			} 			
		}
	}
}
