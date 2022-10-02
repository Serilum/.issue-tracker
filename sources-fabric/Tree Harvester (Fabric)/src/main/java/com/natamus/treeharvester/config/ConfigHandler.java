/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.19.2, mod version: 5.8.
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

package com.natamus.treeharvester.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_mustHoldAxeForTreeHarvest;
	@Entry public static boolean mustHoldAxeForTreeHarvest = true;

	@Comment public static Comment DESC_treeHarvestWithoutSneak;
	@Entry public static boolean treeHarvestWithoutSneak = false;

	@Comment public static Comment DESC_instantBreakLeavesAround;
	@Entry public static boolean instantBreakLeavesAround = false;

	@Comment public static Comment DESC_automaticallyFindBottomBlock;
	@Entry public static boolean automaticallyFindBottomBlock = true;

	@Comment public static Comment DESC_enableFastLeafDecay;
	@Entry public static boolean enableFastLeafDecay = true;

	@Comment public static Comment DESC_enableNetherTrees;
	@Entry public static boolean enableNetherTrees = true;

	@Comment public static Comment DESC_enableHugeMushrooms;
	@Entry public static boolean enableHugeMushrooms = true;

	@Comment public static Comment DESC_replaceSaplingOnTreeHarvest;
	@Entry public static boolean replaceSaplingOnTreeHarvest = true;

	@Comment public static Comment DESC_replaceMushroomOnMushroomHarvest;
	@Entry public static boolean replaceMushroomOnMushroomHarvest = true;

	@Comment public static Comment DESC_loseDurabilityPerHarvestedLog;
	@Entry public static boolean loseDurabilityPerHarvestedLog = true;

	@Comment public static Comment DESC_loseDurabilityModifier;
	@Entry public static double loseDurabilityModifier = 1.0;
	@Comment public static Comment RANGE_loseDurabilityModifier;

	@Comment public static Comment DESC_increaseExhaustionPerHarvestedLog;
	@Entry public static boolean increaseExhaustionPerHarvestedLog = true;

	@Comment public static Comment DESC_increaseExhaustionModifier;
	@Entry public static double increaseExhaustionModifier = 1.0;
	@Comment public static Comment RANGE_increaseExhaustionModifier;

	@Comment public static Comment DESC_increaseHarvestingTimePerLog;
	@Entry public static boolean increaseHarvestingTimePerLog = true;

	@Comment public static Comment DESC_increasedHarvestingTimePerLogModifier;
	@Entry public static double increasedHarvestingTimePerLogModifier = 0.1;
	@Comment public static Comment RANGE_increasedHarvestingTimePerLogModifier;

	@Comment public static Comment DESC_amountOfLeavesBrokenPerTick;
	@Entry public static int amountOfLeavesBrokenPerTick = 3;
	@Comment public static Comment RANGE_amountOfLeavesBrokenPerTick;
}