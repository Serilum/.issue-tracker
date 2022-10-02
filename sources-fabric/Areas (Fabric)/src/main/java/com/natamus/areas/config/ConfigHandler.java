/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.2, mod version: 3.2.
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

package com.natamus.areas.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_giveUnnamedAreasRandomName;
	@Entry public static boolean giveUnnamedAreasRandomName = true;

	@Comment public static Comment DESC_radiusAroundPlayerToCheckForSigns;
	@Entry public static int radiusAroundPlayerToCheckForSigns = 100;
	@Comment public static Comment RANGE_radiusAroundPlayerToCheckForSigns;

	@Comment public static Comment DESC_sendChatMessages;
	@Entry public static boolean sendChatMessages = false;

	@Comment public static Comment DESC_showHUDMessages;
	@Entry public static boolean showHUDMessages = true;

	@Comment public static Comment DESC_joinPrefix;
	@Entry public static String joinPrefix = "Entering ";

	@Comment public static Comment DESC_joinSuffix;
	@Entry public static String joinSuffix = ".";

	@Comment public static Comment DESC_leavePrefix;
	@Entry public static String leavePrefix = "Leaving ";

	@Comment public static Comment DESC_leaveSuffix;
	@Entry public static String leaveSuffix = ".";

	@Comment public static Comment DESC_HUDOnlyAreaName;
	@Entry public static boolean HUDOnlyAreaName = false;

	@Comment public static Comment DESC_HUDMessageFadeDelayMs;
	@Entry public static int HUDMessageFadeDelayMs = 4000;
	@Comment public static Comment RANGE_HUDMessageFadeDelayMs;

	@Comment public static Comment DESC_HUDMessageHeightOffset;
	@Entry public static int HUDMessageHeightOffset = 10;
	@Comment public static Comment RANGE_HUDMessageHeightOffset;

	@Comment public static Comment DESC_HUD_FontSizeScaleModifier;
	@Entry public static double HUD_FontSizeScaleModifier = 1.0;
	@Comment public static Comment RANGE_HUD_FontSizeScaleModifier;

	@Comment public static Comment DESC_HUD_RGB_R;
	@Entry public static int HUD_RGB_R = 100;
	@Comment public static Comment RANGE_HUD_RGB_R;

	@Comment public static Comment DESC_HUD_RGB_G;
	@Entry public static int HUD_RGB_G = 200;
	@Comment public static Comment RANGE_HUD_RGB_G;

	@Comment public static Comment DESC_HUD_RGB_B;
	@Entry public static int HUD_RGB_B = 50;
	@Comment public static Comment RANGE_HUD_RGB_B;
}