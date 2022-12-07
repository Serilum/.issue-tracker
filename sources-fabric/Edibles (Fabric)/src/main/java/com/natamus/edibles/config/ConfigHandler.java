/*
 * This is the latest source code of Edibles.
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

package com.natamus.edibles.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_maxItemUsesPerDaySingleItem;
	@Entry public static int maxItemUsesPerDaySingleItem = 16;
	@Comment public static Comment RANGE_maxItemUsesPerDaySingleItem;

	@Comment public static Comment DESC_maxItemUsesPerDayTotal;
	@Entry public static int maxItemUsesPerDayTotal = -1;
	@Comment public static Comment RANGE_maxItemUsesPerDayTotal;

	@Comment public static Comment DESC_weaknessDurationSeconds;
	@Entry public static int weaknessDurationSeconds = 45;
	@Comment public static Comment RANGE_weaknessDurationSeconds;

	@Comment public static Comment DESC_glowEntityDurationSeconds;
	@Entry public static int glowEntityDurationSeconds = 20;
	@Comment public static Comment RANGE_glowEntityDurationSeconds;

	@Comment public static Comment DESC_glowEntitiesAroundAffectedRadiusBlocks;
	@Entry public static int glowEntitiesAroundAffectedRadiusBlocks = 32;
	@Comment public static Comment RANGE_glowEntitiesAroundAffectedRadiusBlocks;

	@Comment public static Comment DESC__cooldownInMsBetweenUses;
	@Entry public static int _cooldownInMsBetweenUses = 1000;
	@Comment public static Comment RANGE__cooldownInMsBetweenUses;

	@Comment public static Comment DESC_blazePowderStrengthDurationSeconds;
	@Entry public static int blazePowderStrengthDurationSeconds = 15;
	@Comment public static Comment RANGE_blazePowderStrengthDurationSeconds;

	@Comment public static Comment DESC_magmaCreamFireResistanceDurationSeconds;
	@Entry public static int magmaCreamFireResistanceDurationSeconds = 15;
	@Comment public static Comment RANGE_magmaCreamFireResistanceDurationSeconds;

	@Comment public static Comment DESC_sugarSpeedDurationSeconds;
	@Entry public static int sugarSpeedDurationSeconds = 15;
	@Comment public static Comment RANGE_sugarSpeedDurationSeconds;

	@Comment public static Comment DESC_ghastTearDurationSeconds;
	@Entry public static int ghastTearDurationSeconds = 15;
	@Comment public static Comment RANGE_ghastTearDurationSeconds;

	@Comment public static Comment DESC_phantomMembraneDurationSeconds;
	@Entry public static int phantomMembraneDurationSeconds = 15;
	@Comment public static Comment RANGE_phantomMembraneDurationSeconds;

	@Comment public static Comment DESC_rabbitsFootDurationSeconds;
	@Entry public static int rabbitsFootDurationSeconds = 15;
	@Comment public static Comment RANGE_rabbitsFootDurationSeconds;
}