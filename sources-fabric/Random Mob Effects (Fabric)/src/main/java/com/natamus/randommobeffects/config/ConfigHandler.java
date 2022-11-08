/*
 * This is the latest source code of Random Mob Effects.
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

package com.natamus.randommobeffects.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_potionEffectLevel;
	@Entry public static int potionEffectLevel = 1;
	@Comment public static Comment RANGE_potionEffectLevel;

	@Comment public static Comment DESC_hideEffectParticles;
	@Entry public static boolean hideEffectParticles = false;

	@Comment public static Comment DESC_disableCreepers;
	@Entry public static boolean disableCreepers = true;
}