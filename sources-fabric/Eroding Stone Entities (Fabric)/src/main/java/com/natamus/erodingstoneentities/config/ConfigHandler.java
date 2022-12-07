/*
 * This is the latest source code of Eroding Stone Entities.
 * Minecraft version: 1.19.3, mod version: 3.1.
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

package com.natamus.erodingstoneentities.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_durationInSecondsStoneErodes;
	@Entry public static int durationInSecondsStoneErodes = 150;
	@Comment public static Comment RANGE_durationInSecondsStoneErodes;

	@Comment public static Comment DESC_preventErosionIfAboveIceBlock;
	@Entry public static boolean preventErosionIfAboveIceBlock = true;

	@Comment public static Comment DESC_erodeIntoClayBlockInsteadOfClayBall;
	@Entry public static boolean erodeIntoClayBlockInsteadOfClayBall = false;

	@Comment public static Comment DESC_itemsWhichErodeIntoSand;
	@Entry public static String itemsWhichErodeIntoSand = "minecraft:cobblestone,minecraft:mossy_cobblestone,minecraft:stone,minecraft:stone_bricks,minecraft:chiseled_stone_bricks,minecraft:cracked_stone_bricks,minecraft:smooth_stone,minecraft:gravel,minecraft:andesite,minecraft:polished_andesite,minecraft:diorite,minecraft:polished_diorite,minecraft:granite,minecraft:polished_granite,minecraft:sandstone,minecraft:chiseled_sandstone,minecraft:cut_sandstone,minecraft:smooth_sandstone";

	@Comment public static Comment DESC_itemsWhichErodeIntoRedSand;
	@Entry public static String itemsWhichErodeIntoRedSand = "minecraft:red_sandstone,minecraft:chiseled_red_sandstone,minecraft:cut_red_sandstone,minecraft:smooth_red_sandstone,minecraft:netherrack,minecraft:nether_bricks,minecraft:red_nether_bricks";

	@Comment public static Comment DESC_itemsWhichErodeIntoClay;
	@Entry public static String itemsWhichErodeIntoClay = "minecraft:terracotta,minecraft:white_terracotta,minecraft:orange_terracotta,minecraft:magenta_terracotta,minecraft:light_blue_terracotta,minecraft:yellow_terracotta,minecraft:lime_terracotta,minecraft:pink_terracotta,minecraft:gray_terracotta,minecraft:light_gray_terracotta,minecraft:cyan_terracotta,minecraft:purple_terracotta,minecraft:blue_terracotta,minecraft:brown_terracotta,minecraft:green_terracotta,minecraft:red_terracotta,minecraft:black_terracotta";
}