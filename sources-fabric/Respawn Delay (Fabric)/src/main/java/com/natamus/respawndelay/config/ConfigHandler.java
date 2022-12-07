/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.19.3, mod version: 3.3.
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

package com.natamus.respawndelay.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_ignoreAdministratorPlayers;
	@Entry public static boolean ignoreAdministratorPlayers = false;

	@Comment public static Comment DESC_ignoreCreativePlayers;
	@Entry public static boolean ignoreCreativePlayers = true;

	@Comment public static Comment DESC_keepItemsOnDeath;
	@Entry public static boolean keepItemsOnDeath = false;

	@Comment public static Comment DESC_dropItemsOnDeath;
	@Entry public static boolean dropItemsOnDeath = true;

	@Comment public static Comment DESC_respawnAtWorldSpawn;
	@Entry public static boolean respawnAtWorldSpawn = true;

	@Comment public static Comment DESC_respawnWhenPlayerLogsOut;
	@Entry public static boolean respawnWhenPlayerLogsOut = true;

	@Comment public static Comment DESC_respawnWhenPlayerLogsIn;
	@Entry public static boolean respawnWhenPlayerLogsIn = true;

	@Comment public static Comment DESC_respawnDelayInSeconds;
	@Entry public static int respawnDelayInSeconds = 10;
	@Comment public static Comment RANGE_respawnDelayInSeconds;

	@Comment public static Comment DESC_onDeathMessage;
	@Entry public static String onDeathMessage = "You died! Your gamemode has been set to spectator.";

	@Comment public static Comment DESC_onRespawnMessage;
	@Entry public static String onRespawnMessage = "You respawned.";

	@Comment public static Comment DESC_waitingForRespawnMessage;
	@Entry public static String waitingForRespawnMessage = "You will respawn in <seconds_left> seconds.";

	@Comment public static Comment DESC_lowestPossibleYCoordinate;
	@Entry public static int lowestPossibleYCoordinate = -10;
	@Comment public static Comment RANGE_lowestPossibleYCoordinate;
}