/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.19.2, mod version: 3.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.advancementscreenshot.util;

public class Util {
	public static boolean takescreenshot = false;
	public static int cooldown = -1;
	
	public static void takeScreenshot() {
		takescreenshot = true;
		cooldown = 20;
	}
}
