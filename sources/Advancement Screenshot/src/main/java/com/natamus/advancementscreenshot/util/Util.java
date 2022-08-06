/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.19.2, mod version: 3.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
