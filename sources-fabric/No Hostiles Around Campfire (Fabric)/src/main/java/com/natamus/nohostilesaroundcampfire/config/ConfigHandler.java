/*
 * This is the latest source code of No Hostiles Around Campfire.
 * Minecraft version: 1.19.2, mod version: 4.6.
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

package com.natamus.nohostilesaroundcampfire.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_preventHostilesRadius;
	@Entry public static int preventHostilesRadius = 48;
	@Comment public static Comment RANGE_preventHostilesRadius;

	@Comment public static Comment DESC_burnHostilesAroundWhenPlaced;
	@Entry public static boolean burnHostilesAroundWhenPlaced = true;

	@Comment public static Comment DESC_burnHostilesRadiusModifier;
	@Entry public static double burnHostilesRadiusModifier = 0.5;
	@Comment public static Comment RANGE_burnHostilesRadiusModifier;

	@Comment public static Comment DESC_preventMobSpawnerSpawns;
	@Entry public static boolean preventMobSpawnerSpawns = false;

	@Comment public static Comment DESC_campfireMustBeLit;
	@Entry public static boolean campfireMustBeLit = true;

	@Comment public static Comment DESC_campfireMustBeSignalling;
	@Entry public static boolean campfireMustBeSignalling = false;

	@Comment public static Comment DESC_enableEffectForNormalCampfires;
	@Entry public static boolean enableEffectForNormalCampfires = true;

	@Comment public static Comment DESC_enableEffectForSoulCampfires;
	@Entry public static boolean enableEffectForSoulCampfires = true;
}