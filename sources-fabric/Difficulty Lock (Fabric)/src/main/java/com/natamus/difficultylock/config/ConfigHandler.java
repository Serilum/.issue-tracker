/*
 * This is the latest source code of Difficulty Lock.
 * Minecraft version: 1.19.3, mod version: 2.0.
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

package com.natamus.difficultylock.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_forcePeaceful;
	@Entry public static boolean forcePeaceful = false;

	@Comment public static Comment DESC_forceEasy;
	@Entry public static boolean forceEasy = false;

	@Comment public static Comment DESC_forceNormal;
	@Entry public static boolean forceNormal = false;

	@Comment public static Comment DESC_forceHard;
	@Entry public static boolean forceHard = true;

	@Comment public static Comment DESC_shouldLockDifficulty;
	@Entry public static boolean shouldLockDifficulty = true;

	@Comment public static Comment DESC_shouldChangeDifficultyWhenAlreadyLocked;
	@Entry public static boolean shouldChangeDifficultyWhenAlreadyLocked = false;
}