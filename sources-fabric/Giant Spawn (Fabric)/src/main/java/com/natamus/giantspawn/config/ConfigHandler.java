/*
 * This is the latest source code of Giant Spawn.
 * Minecraft version: 1.19.3, mod version: 3.4.
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

package com.natamus.giantspawn.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_chanceSurfaceZombieIsGiant;
	@Entry public static double chanceSurfaceZombieIsGiant = 0.05;
	@Comment public static Comment RANGE_chanceSurfaceZombieIsGiant;

	@Comment public static Comment DESC_shouldBurnGiantsInDaylight;
	@Entry public static boolean shouldBurnGiantsInDaylight = true;

	@Comment public static Comment DESC_onlySpawnGiantOnSurface;
	@Entry public static boolean onlySpawnGiantOnSurface = true;

	@Comment public static Comment DESC_giantMovementSpeedModifier;
	@Entry public static double giantMovementSpeedModifier = 1.0;
	@Comment public static Comment RANGE_giantMovementSpeedModifier;

	@Comment public static Comment DESC_giantAttackDamageModifier;
	@Entry public static double giantAttackDamageModifier = 2.0;
	@Comment public static Comment RANGE_giantAttackDamageModifier;
}