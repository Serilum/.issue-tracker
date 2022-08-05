/*
 * This is the latest source code of Fire Spread Tweaks.
 * Minecraft version: 1.19.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.firespreadtweaks.util;

import com.natamus.collective_fabric.data.GlobalVariables;
import com.natamus.firespreadtweaks.config.ConfigHandler;

public class Util {
	public static int getFireBurnDurationInTicks() {
		int duration = ConfigHandler.timeFireBurnsInTicks.getValue();
		if (ConfigHandler.enableRandomizedFireDuration.getValue()) {
			int min = ConfigHandler.MinRandomExtraBurnTicks.getValue();
			int max = ConfigHandler.MaxRandomExtraBurnTicks.getValue();
			
			int randomized = GlobalVariables.random.nextInt(max-min) + min;
			duration += randomized;
		}
		
		return duration;
	}
}
