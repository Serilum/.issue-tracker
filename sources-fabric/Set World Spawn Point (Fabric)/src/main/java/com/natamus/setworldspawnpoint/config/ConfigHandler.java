/*
 * This is the latest source code of Set World Spawn Point.
 * Minecraft version: 1.19.2, mod version: 2.7.
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

package com.natamus.setworldspawnpoint.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC__forceExactSpawn;
	@Entry public static boolean _forceExactSpawn = true;

	@Comment public static Comment DESC_xCoordSpawnPoint;
	@Entry public static int xCoordSpawnPoint = 0;
	@Comment public static Comment RANGE_xCoordSpawnPoint;

	@Comment public static Comment DESC_yCoordSpawnPoint;
	@Entry public static int yCoordSpawnPoint = -1;
	@Comment public static Comment RANGE_yCoordSpawnPoint;

	@Comment public static Comment DESC_zCoordSpawnPoint;
	@Entry public static int zCoordSpawnPoint = 0;
	@Comment public static Comment RANGE_zCoordSpawnPoint;
}