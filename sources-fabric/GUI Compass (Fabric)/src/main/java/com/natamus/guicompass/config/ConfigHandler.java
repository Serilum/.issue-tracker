/*
 * This is the latest source code of GUI Compass.
 * Minecraft version: 1.19.3, mod version: 3.0.
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

package com.natamus.guicompass.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_guiCompassFormat;
	@Entry public static String guiCompassFormat = "FXYZ";

	@Comment public static Comment DESC_mustHaveCompassInInventory;
	@Entry public static boolean mustHaveCompassInInventory = true;

	@Comment public static Comment DESC_compassPositionIsLeft;
	@Entry public static boolean compassPositionIsLeft = true;

	@Comment public static Comment DESC_compassPositionIsCenter;
	@Entry public static boolean compassPositionIsCenter = false;

	@Comment public static Comment DESC_compassPositionIsRight;
	@Entry public static boolean compassPositionIsRight = false;

	@Comment public static Comment DESC_compassHeightOffset;
	@Entry public static int compassHeightOffset = 5;
	@Comment public static Comment RANGE_compassHeightOffset;

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