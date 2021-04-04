/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.util;

import java.util.List;

import com.natamus.collective.functions.DataFunctions;

public class TveUtil {
	private static List<String> installedmodjars = DataFunctions.getInstalledModJars();
	
	public static boolean shouldLoadMod(String modname) {
		String jarpart = modname.toLowerCase().replaceAll(" ", "");
		for (String modjar : installedmodjars) {
			if (modjar.equalsIgnoreCase(jarpart + "_")) {
				System.out.println("Disabled " + modname + " in The Vanilla Experience mod because it already exists as a separate mod.");
				return false;
			}
		}
		
		return true;
	}
}
