/*
 * This is the latest source code of GUI Followers.
 * Minecraft version: 1.19.2, mod version: 2.4.
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

package com.natamus.guifollowers.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_followerListHeaderFormat;
	@Entry public static String followerListHeaderFormat = "Followers:";

	@Comment public static Comment DESC_showFollowerHealth;
	@Entry public static boolean showFollowerHealth = true;

	@Comment public static Comment DESC_followerHealthFormat;
	@Entry public static String followerHealthFormat = ": <health>%";

	@Comment public static Comment DESC_showFollowerDistance;
	@Entry public static boolean showFollowerDistance = true;

	@Comment public static Comment DESC_followerDistanceFormat;
	@Entry public static String followerDistanceFormat = " (<distance> blocks)";

	@Comment public static Comment DESC_distanceToCheckForFollowersAround;
	@Entry public static int distanceToCheckForFollowersAround = 50;
	@Comment public static Comment RANGE_distanceToCheckForFollowersAround;

	@Comment public static Comment DESC_timeBetweenChecksInSeconds;
	@Entry public static int timeBetweenChecksInSeconds = 2;
	@Comment public static Comment RANGE_timeBetweenChecksInSeconds;

	@Comment public static Comment DESC_followerListPositionIsLeft;
	@Entry public static boolean followerListPositionIsLeft = true;

	@Comment public static Comment DESC_followerListPositionIsCenter;
	@Entry public static boolean followerListPositionIsCenter = false;

	@Comment public static Comment DESC_followerListPositionIsRight;
	@Entry public static boolean followerListPositionIsRight = false;

	@Comment public static Comment DESC_followerListHeightOffset;
	@Entry public static int followerListHeightOffset = 20;
	@Comment public static Comment RANGE_followerListHeightOffset;

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