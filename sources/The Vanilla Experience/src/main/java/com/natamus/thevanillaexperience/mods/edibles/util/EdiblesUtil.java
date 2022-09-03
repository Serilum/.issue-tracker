/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.edibles.util;

import java.util.Date;
import java.util.HashMap;

import com.natamus.thevanillaexperience.mods.edibles.config.EdiblesConfigHandler;

public class EdiblesUtil {
	private static HashMap<String, Date> playerlastuse = new HashMap<String, Date>();
	
	public static boolean canPlayerUse(String playername) {
		if (!playerlastuse.containsKey(playername)) {
			return true;
		}
		
		int mscooldown = EdiblesConfigHandler.OTHER._cooldownInMsBetweenUses.get();
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
