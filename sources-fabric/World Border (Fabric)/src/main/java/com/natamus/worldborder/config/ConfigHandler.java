/*
 * This is the latest source code of World Border.
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

package com.natamus.worldborder.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_enableCustomOverworldBorder;
	@Entry public static boolean enableCustomOverworldBorder = true;

	@Comment public static Comment DESC_enableCustomNetherBorder;
	@Entry public static boolean enableCustomNetherBorder = false;

	@Comment public static Comment DESC_enableCustomEndBorder;
	@Entry public static boolean enableCustomEndBorder = true;

	@Comment public static Comment DESC_shouldLoopToOppositeBorder;
	@Entry public static boolean shouldLoopToOppositeBorder = true;

	@Comment public static Comment DESC_distanceTeleportedBack;
	@Entry public static int distanceTeleportedBack = 10;
	@Comment public static Comment RANGE_distanceTeleportedBack;

	@Comment public static Comment DESC_nearBorderMessage;
	@Entry public static String nearBorderMessage = "You're getting close to the world border!";

	@Comment public static Comment DESC_hitBorderMessage;
	@Entry public static String hitBorderMessage = "You've hit the world border, and were teleported inside!";

	@Comment public static Comment DESC_loopBorderMessage;
	@Entry public static String loopBorderMessage = "You've hit the world border, and have looped around the world!";

	@Comment public static Comment DESC_overworldBorderPositiveX;
	@Entry public static int overworldBorderPositiveX = 5000;
	@Comment public static Comment RANGE_overworldBorderPositiveX;

	@Comment public static Comment DESC_overworldBorderNegativeX;
	@Entry public static int overworldBorderNegativeX = -5000;
	@Comment public static Comment RANGE_overworldBorderNegativeX;

	@Comment public static Comment DESC_overworldBorderPositiveZ;
	@Entry public static int overworldBorderPositiveZ = 5000;
	@Comment public static Comment RANGE_overworldBorderPositiveZ;

	@Comment public static Comment DESC_overworldBorderNegativeZ;
	@Entry public static int overworldBorderNegativeZ = -5000;
	@Comment public static Comment RANGE_overworldBorderNegativeZ;

	@Comment public static Comment DESC_netherBorderPositiveX;
	@Entry public static int netherBorderPositiveX = 625;
	@Comment public static Comment RANGE_netherBorderPositiveX;

	@Comment public static Comment DESC_netherBorderNegativeX;
	@Entry public static int netherBorderNegativeX = -625;
	@Comment public static Comment RANGE_netherBorderNegativeX;

	@Comment public static Comment DESC_netherBorderPositiveZ;
	@Entry public static int netherBorderPositiveZ = 625;
	@Comment public static Comment RANGE_netherBorderPositiveZ;

	@Comment public static Comment DESC_netherBorderNegativeZ;
	@Entry public static int netherBorderNegativeZ = -625;
	@Comment public static Comment RANGE_netherBorderNegativeZ;

	@Comment public static Comment DESC_endBorderPositiveX;
	@Entry public static int endBorderPositiveX = 5000;
	@Comment public static Comment RANGE_endBorderPositiveX;

	@Comment public static Comment DESC_endBorderNegativeX;
	@Entry public static int endBorderNegativeX = -5000;
	@Comment public static Comment RANGE_endBorderNegativeX;

	@Comment public static Comment DESC_endBorderPositiveZ;
	@Entry public static int endBorderPositiveZ = 5000;
	@Comment public static Comment RANGE_endBorderPositiveZ;

	@Comment public static Comment DESC_endBorderNegativeZ;
	@Entry public static int endBorderNegativeZ = -5000;
	@Comment public static Comment RANGE_endBorderNegativeZ;
}