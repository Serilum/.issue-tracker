/*
 * This is the latest source code of GUI Clock.
 * Minecraft version: 1.19.2, mod version: 3.8.
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

package com.natamus.guiclock.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_mustHaveClockInInventoryForGameTime;
	@Entry public static boolean mustHaveClockInInventoryForGameTime = true;

	@Comment public static Comment DESC_mustHaveClockInInventoryForRealTime;
	@Entry public static boolean mustHaveClockInInventoryForRealTime = true;

	@Comment public static Comment DESC_lowerClockWhenPlayerHasEffects;
	@Entry public static boolean lowerClockWhenPlayerHasEffects = true;

	@Comment public static Comment DESC__24hourformat;
	@Entry public static boolean _24hourformat = true;

	@Comment public static Comment DESC_showOnlyMinecraftClockIcon;
	@Entry public static boolean showOnlyMinecraftClockIcon = false;

	@Comment public static Comment DESC_showBothTimes;
	@Entry public static boolean showBothTimes = false;

	@Comment public static Comment DESC_showRealTime;
	@Entry public static boolean showRealTime = false;

	@Comment public static Comment DESC_showRealTimeSeconds;
	@Entry public static boolean showRealTimeSeconds = false;

	@Comment public static Comment DESC_showDaysPlayedWorld;
	@Entry public static boolean showDaysPlayedWorld = true;

	@Comment public static Comment DESC_clockPositionIsLeft;
	@Entry public static boolean clockPositionIsLeft = false;

	@Comment public static Comment DESC_clockPositionIsCenter;
	@Entry public static boolean clockPositionIsCenter = false;

	@Comment public static Comment DESC_clockPositionIsRight;
	@Entry public static boolean clockPositionIsRight = true;

	@Comment public static Comment DESC_clockHeightOffset;
	@Entry public static int clockHeightOffset = 5;
	@Comment public static Comment RANGE_clockHeightOffset;

	@Comment public static Comment DESC_clockWidthOffset;
	@Entry public static int clockWidthOffset = 0;
	@Comment public static Comment RANGE_clockWidthOffset;

	@Comment public static Comment DESC_RGB_R;
	@Entry public static int RGB_R = 255;
	@Comment public static Comment RANGE_RGB_R;

	@Comment public static Comment DESC_RGB_G;
	@Entry public static int RGB_G = 255;
	@Comment public static Comment RANGE_RGB_G;

	@Comment public static Comment DESC_RGB_B;
	@Entry public static int RGB_B = 255;
	@Comment public static Comment RANGE_RGB_B;
}