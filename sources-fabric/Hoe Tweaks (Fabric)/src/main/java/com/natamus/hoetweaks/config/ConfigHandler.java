/*
 * This is the latest source code of Hoe Tweaks.
 * Minecraft version: 1.19.3, mod version: 2.1.
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

package com.natamus.hoetweaks.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_onlyUntillWithOtherHandEmpty;
	@Entry public static boolean onlyUntillWithOtherHandEmpty = true;

	@Comment public static Comment DESC_cropBlockBreakSpeedModifier;
	@Entry public static double cropBlockBreakSpeedModifier = 8.0;
	@Comment public static Comment RANGE_cropBlockBreakSpeedModifier;

	@Comment public static Comment DESC_mustCrouchToHaveBiggerHoeRange;
	@Entry public static boolean mustCrouchToHaveBiggerHoeRange = true;

	@Comment public static Comment DESC_woodenTierHoeRange;
	@Entry public static int woodenTierHoeRange = 0;
	@Comment public static Comment RANGE_woodenTierHoeRange;

	@Comment public static Comment DESC_stoneTierHoeRange;
	@Entry public static int stoneTierHoeRange = 1;
	@Comment public static Comment RANGE_stoneTierHoeRange;

	@Comment public static Comment DESC_goldTierHoeRange;
	@Entry public static int goldTierHoeRange = 2;
	@Comment public static Comment RANGE_goldTierHoeRange;

	@Comment public static Comment DESC_ironTierHoeRange;
	@Entry public static int ironTierHoeRange = 2;
	@Comment public static Comment RANGE_ironTierHoeRange;

	@Comment public static Comment DESC_diamondTierHoeRange;
	@Entry public static int diamondTierHoeRange = 3;
	@Comment public static Comment RANGE_diamondTierHoeRange;

	@Comment public static Comment DESC_netheriteTierHoeRange;
	@Entry public static int netheriteTierHoeRange = 4;
	@Comment public static Comment RANGE_netheriteTierHoeRange;
}