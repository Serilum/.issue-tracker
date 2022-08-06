/*
 * This is the latest source code of Edibles.
 * Minecraft version: 1.19.2, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
