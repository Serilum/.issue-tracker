/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.19.2, mod version: 3.0.
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

package com.natamus.hidehands.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_alwaysHideMainHand;
	@Entry public static boolean alwaysHideMainHand = false;

	@Comment public static Comment DESC_alwaysHideEmptyMainHand;
	@Entry public static boolean alwaysHideEmptyMainHand = true;

	@Comment public static Comment DESC_hideMainHandWithItems;
	@Entry public static String hideMainHandWithItems = "";

	@Comment public static Comment DESC_alwaysHideOffhand;
	@Entry public static boolean alwaysHideOffhand = false;

	@Comment public static Comment DESC_hideOffhandWithItems;
	@Entry public static String hideOffhandWithItems = "minecraft:totem_of_undying,minecraft:torch";
}