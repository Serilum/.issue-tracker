/*
 * This is the latest source code of Better Beacon Placement.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.betterbeaconplacement.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_breakBeaconBaseBlocks;
	@Entry public static boolean breakBeaconBaseBlocks = true;

	@Comment public static Comment DESC_dropReplacedBlockTopBeacon;
	@Entry public static boolean dropReplacedBlockTopBeacon = true;
	
	@Comment public static Comment DESC_maxBeaconLayers;
	@Entry public static int maxBeaconLayers = 4;
}
