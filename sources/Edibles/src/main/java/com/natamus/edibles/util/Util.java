/*
 * This is the latest source code of Edibles.
 * Minecraft version: 1.17.1, mod version: 2.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Edibles ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.edibles.util;

import java.util.Date;
import java.util.HashMap;

import com.natamus.edibles.config.ConfigHandler;

public class Util {
	private static HashMap<String, Date> playerlastuse = new HashMap<String, Date>();
	
	public static boolean canPlayerUse(String playername) {
		if (!playerlastuse.containsKey(playername)) {
			return true;
		}
		
		int mscooldown = ConfigHandler.OTHER._cooldownInMsBetweenUses.get();
		if (mscooldown == 0) {
			return true;
		}
		
		Date now = new Date();
		Date lastuse = playerlastuse.get(playername);
		long ms = (now.getTime()-lastuse.getTime());
		if (ms >= mscooldown) {
			return true;
		}
		
		return false;
	}
	
	public static void setPlayerUse(String playername) {
		playerlastuse.put(playername, new Date());
	}
}
