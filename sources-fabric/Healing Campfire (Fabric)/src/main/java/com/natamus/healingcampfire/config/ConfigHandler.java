/*
 * This is the latest source code of Healing Campfire.
 * Minecraft version: 1.19.2, mod version: 4.0.
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

package com.natamus.healingcampfire.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_checkForCampfireDelayInTicks;
	@Entry public static int checkForCampfireDelayInTicks = 40;
	@Comment public static Comment RANGE_checkForCampfireDelayInTicks;

	@Comment public static Comment DESC_healingRadius;
	@Entry public static int healingRadius = 16;
	@Comment public static Comment RANGE_healingRadius;

	@Comment public static Comment DESC_effectDurationSeconds;
	@Entry public static int effectDurationSeconds = 60;
	@Comment public static Comment RANGE_effectDurationSeconds;

	@Comment public static Comment DESC_regenerationLevel;
	@Entry public static int regenerationLevel = 1;
	@Comment public static Comment RANGE_regenerationLevel;

	@Comment public static Comment DESC_healPassiveMobs;
	@Entry public static boolean healPassiveMobs = true;

	@Comment public static Comment DESC_hideEffectParticles;
	@Entry public static boolean hideEffectParticles = true;

	@Comment public static Comment DESC_campfireMustBeLit;
	@Entry public static boolean campfireMustBeLit = true;

	@Comment public static Comment DESC_campfireMustBeSignalling;
	@Entry public static boolean campfireMustBeSignalling = false;

	@Comment public static Comment DESC_enableEffectForNormalCampfires;
	@Entry public static boolean enableEffectForNormalCampfires = true;

	@Comment public static Comment DESC_enableEffectForSoulCampfires;
	@Entry public static boolean enableEffectForSoulCampfires = true;
}