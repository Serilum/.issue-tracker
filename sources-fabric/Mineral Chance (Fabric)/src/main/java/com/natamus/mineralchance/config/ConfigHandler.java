/*
 * This is the latest source code of Mineral Chance.
 * Minecraft version: 1.19.2, mod version: 2.2.
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

package com.natamus.mineralchance.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_extraMineralChanceOnOverworldStoneBreak;
	@Entry public static double extraMineralChanceOnOverworldStoneBreak = 0.02;
	@Comment public static Comment RANGE_extraMineralChanceOnOverworldStoneBreak;

	@Comment public static Comment DESC_extraMineralChanceOnNetherStoneBreak;
	@Entry public static double extraMineralChanceOnNetherStoneBreak = 0.01;
	@Comment public static Comment RANGE_extraMineralChanceOnNetherStoneBreak;

	@Comment public static Comment DESC_enableOverworldMinerals;
	@Entry public static boolean enableOverworldMinerals = true;

	@Comment public static Comment DESC_enableNetherMinerals;
	@Entry public static boolean enableNetherMinerals = true;

	@Comment public static Comment DESC_sendMessageOnMineralFind;
	@Entry public static boolean sendMessageOnMineralFind = true;

	@Comment public static Comment DESC_foundMineralMessage;
	@Entry public static String foundMineralMessage = "You've found a mineral hidden in the block!";

	@Comment public static Comment DESC_ignoreFakePlayers;
	@Entry public static boolean ignoreFakePlayers = true;
}