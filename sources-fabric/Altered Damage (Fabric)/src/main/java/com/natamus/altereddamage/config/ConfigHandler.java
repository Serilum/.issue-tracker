/*
 * This is the latest source code of Altered Damage.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.altereddamage.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_preventFatalModifiedDamage;
	@Entry public static boolean preventFatalModifiedDamage = true;

	@Comment public static Comment DESC_alterEntityDamageTaken;
	@Entry public static boolean alterEntityDamageTaken = true;

	@Comment public static Comment DESC_alterPlayerDamageTaken;
	@Entry public static boolean alterPlayerDamageTaken = true;

	@Comment public static Comment DESC_entityDamageModifier;
	@Entry public static double entityDamageModifier = 2.0;
	@Comment public static Comment RANGE_entityDamageModifier;

	@Comment public static Comment DESC_playerDamageModifier;
	@Entry public static double playerDamageModifier = 2.0;
	@Comment public static Comment RANGE_playerDamageModifier;
}