/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
