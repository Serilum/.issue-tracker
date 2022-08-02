/*
 * This is the latest source code of Full Brightness Toggle.
 * Minecraft version: 1.19.1, mod version: 1.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.fullbrightnesstoggle.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.ChatScreen;

public class ToggleEvent {
	private static Minecraft mc = null;
	private static double initialgamma = -1;
	private static double maxgamma = 14.0F % 28.0F + 1.0F;
	
	public static void onHotkeyPress() {
		if (mc == null) {
			mc = Minecraft.getInstance();
		}
		
		if (mc.screen instanceof ChatScreen) {
			return;
		}
		
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
