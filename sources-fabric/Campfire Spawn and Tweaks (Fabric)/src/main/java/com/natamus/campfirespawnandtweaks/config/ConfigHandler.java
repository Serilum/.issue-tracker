/*
 * This is the latest source code of Campfire Spawn and Tweaks.
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

package com.natamus.campfirespawnandtweaks.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_campfiresStartUnlit;
	@Entry public static boolean campfiresStartUnlit = true;

	@Comment public static Comment DESC_sneakRightClickCampfireToUnset;
	@Entry public static boolean sneakRightClickCampfireToUnset = true;

	@Comment public static Comment DESC_bedsOverrideCampfireSpawnOnSneakRightClick;
	@Entry public static boolean bedsOverrideCampfireSpawnOnSneakRightClick = true;

	@Comment public static Comment DESC_createAirPocketIfBlocksAboveCampfire;
	@Entry public static boolean createAirPocketIfBlocksAboveCampfire = true;

	@Comment public static Comment DESC_sendMessageOnNewCampfireSpawnSet;
	@Entry public static boolean sendMessageOnNewCampfireSpawnSet = true;

	@Comment public static Comment DESC_sendMessageOnCampfireSpawnUnset;
	@Entry public static boolean sendMessageOnCampfireSpawnUnset = true;

	@Comment public static Comment DESC_sendMessageOnCampfireSpawnMissing;
	@Entry public static boolean sendMessageOnCampfireSpawnMissing = true;

	@Comment public static Comment DESC_sendMessageOnCampfireSpawnOverride;
	@Entry public static boolean sendMessageOnCampfireSpawnOverride = true;

	@Comment public static Comment DESC_fireResitanceDurationOnRespawnInMs;
	@Entry public static int fireResitanceDurationOnRespawnInMs = 10000;
	@Comment public static Comment RANGE_fireResitanceDurationOnRespawnInMs;
}