/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.firespreadtweaks.util;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.thevanillaexperience.mods.firespreadtweaks.config.FireSpreadTweaksConfigHandler;

public class FireSpreadTweaksUtil {
	public static int getFireBurnDurationInTicks() {
		int duration = FireSpreadTweaksConfigHandler.GENERAL.timeFireBurnsInTicks.get();
		if (FireSpreadTweaksConfigHandler.GENERAL.enableRandomizedFireDuration.get()) {
			int min = FireSpreadTweaksConfigHandler.GENERAL.MinRandomExtraBurnTicks.get();
			int max = FireSpreadTweaksConfigHandler.GENERAL.MaxRandomExtraBurnTicks.get();
			
			int randomized = GlobalVariables.random.nextInt(max-min) + min;
			duration += randomized;
		}
		
		return duration;
	}
}
