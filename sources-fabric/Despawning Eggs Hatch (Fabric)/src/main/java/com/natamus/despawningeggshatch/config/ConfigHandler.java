/*
 * This is the latest source code of Despawning Eggs Hatch.
 * Minecraft version: 1.19.2, mod version: 2.9.
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

package com.natamus.despawningeggshatch.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_eggOnlyHatchesWhenOnTopOfHayBlock;
	@Entry public static boolean eggOnlyHatchesWhenOnTopOfHayBlock = true;

	@Comment public static Comment DESC_eggWillHatchChance;
	@Entry public static double eggWillHatchChance = 1.0;
	@Comment public static Comment RANGE_eggWillHatchChance;

	@Comment public static Comment DESC_onlyHatchIfLessChickensAroundThan;
	@Entry public static int onlyHatchIfLessChickensAroundThan = 50;
	@Comment public static Comment RANGE_onlyHatchIfLessChickensAroundThan;

	@Comment public static Comment DESC_radiusEntityLimiterCheck;
	@Entry public static int radiusEntityLimiterCheck = 32;
	@Comment public static Comment RANGE_radiusEntityLimiterCheck;

	@Comment public static Comment DESC_newHatchlingIsBaby;
	@Entry public static boolean newHatchlingIsBaby = true;
}