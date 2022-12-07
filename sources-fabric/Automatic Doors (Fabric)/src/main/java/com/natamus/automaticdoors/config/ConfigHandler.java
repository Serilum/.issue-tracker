/*
 * This is the latest source code of Automatic Doors.
 * Minecraft version: 1.19.3, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.automaticdoors.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_doorOpenTime;
	@Entry public static int doorOpenTime = 2500;
	@Comment public static Comment RANGE_doorOpenTime;

	@Comment public static Comment DESC_shouldOpenIronDoors;
	@Entry public static boolean shouldOpenIronDoors = true;

	@Comment public static Comment DESC_preventOpeningOnSneak;
	@Entry public static boolean preventOpeningOnSneak = true;
}