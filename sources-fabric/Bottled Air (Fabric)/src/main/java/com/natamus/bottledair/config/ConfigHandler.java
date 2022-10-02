/*
 * This is the latest source code of Bottled Air.
 * Minecraft version: 1.19.2, mod version: 1.7.
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

package com.natamus.bottledair.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_disableWaterConsumptionUnderwater;
	@Entry public static boolean disableWaterConsumptionUnderwater = true;

	@Comment public static Comment DESC_holdFireTypeItemInOffhandToPreventWaterBottleCreation;
	@Entry public static boolean holdFireTypeItemInOffhandToPreventWaterBottleCreation = true;

	@Comment public static Comment DESC_chanceGlassBottleBreaksWithFireTypeInOffhand;
	@Entry public static double chanceGlassBottleBreaksWithFireTypeInOffhand = 0.5;
	@Comment public static Comment RANGE_chanceGlassBottleBreaksWithFireTypeInOffhand;

	@Comment public static Comment DESC_amountOfAirInBottles;
	@Entry public static int amountOfAirInBottles = 150;
	@Comment public static Comment RANGE_amountOfAirInBottles;
}