/*
 * This is the latest source code of Trample Everything.
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

package com.natamus.trampleeverything.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC__enableTrampledBlockItems;
	@Entry public static boolean _enableTrampledBlockItems = true;

	@Comment public static Comment DESC__crouchingPreventsTrampling;
	@Entry public static boolean _crouchingPreventsTrampling = true;

	@Comment public static Comment DESC_trampleSnow;
	@Entry public static boolean trampleSnow = false;

	@Comment public static Comment DESC_trampleBambooSaplings;
	@Entry public static boolean trampleBambooSaplings = false;

	@Comment public static Comment DESC_trampleCrops;
	@Entry public static boolean trampleCrops = true;

	@Comment public static Comment DESC_trampleDeadBushes;
	@Entry public static boolean trampleDeadBushes = true;

	@Comment public static Comment DESC_trampleDoublePlants;
	@Entry public static boolean trampleDoublePlants = true;

	@Comment public static Comment DESC_trampleFlowers;
	@Entry public static boolean trampleFlowers = true;

	@Comment public static Comment DESC_trampleFungi;
	@Entry public static boolean trampleFungi = true;

	@Comment public static Comment DESC_trampleLilyPads;
	@Entry public static boolean trampleLilyPads = false;

	@Comment public static Comment DESC_trampleMushrooms;
	@Entry public static boolean trampleMushrooms = true;

	@Comment public static Comment DESC_trampleNetherRoots;
	@Entry public static boolean trampleNetherRoots = true;

	@Comment public static Comment DESC_trampleNetherSprouts;
	@Entry public static boolean trampleNetherSprouts = true;

	@Comment public static Comment DESC_trampleNetherWart;
	@Entry public static boolean trampleNetherWart = true;

	@Comment public static Comment DESC_trampleSaplings;
	@Entry public static boolean trampleSaplings = true;

	@Comment public static Comment DESC_trampleSeaGrass;
	@Entry public static boolean trampleSeaGrass = false;

	@Comment public static Comment DESC_trampleSeaPickles;
	@Entry public static boolean trampleSeaPickles = true;

	@Comment public static Comment DESC_trampleStems;
	@Entry public static boolean trampleStems = true;

	@Comment public static Comment DESC_trampleSugarCane;
	@Entry public static boolean trampleSugarCane = false;

	@Comment public static Comment DESC_trampleSweetBerryBushes;
	@Entry public static boolean trampleSweetBerryBushes = false;

	@Comment public static Comment DESC_trampleTallGrass;
	@Entry public static boolean trampleTallGrass = true;
}