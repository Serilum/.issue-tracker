/*
 * This is the latest source code of Double Doors.
 * Minecraft version: 1.19.3, mod version: 3.9.
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

package com.natamus.doubledoors.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_enableRecursiveOpening;
	@Entry public static boolean enableRecursiveOpening = true;

	@Comment public static Comment DESC_recursiveOpeningMaxBlocksDistance;
	@Entry public static int recursiveOpeningMaxBlocksDistance = 10;
	@Comment public static Comment RANGE_recursiveOpeningMaxBlocksDistance;

	@Comment public static Comment DESC_enableDoors;
	@Entry public static boolean enableDoors = true;

	@Comment public static Comment DESC_enableFenceGates;
	@Entry public static boolean enableFenceGates = true;

	@Comment public static Comment DESC_enableTrapdoors;
	@Entry public static boolean enableTrapdoors = true;
}