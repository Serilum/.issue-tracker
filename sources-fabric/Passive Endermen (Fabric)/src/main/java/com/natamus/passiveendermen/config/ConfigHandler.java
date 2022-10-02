/*
 * This is the latest source code of Passive Endermen.
 * Minecraft version: 1.19.2, mod version: 3.2.
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

package com.natamus.passiveendermen.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_preventEndermenFromTeleporting;
	@Entry public static boolean preventEndermenFromTeleporting = true;

	@Comment public static Comment DESC_preventEndermenFromGriefing;
	@Entry public static boolean preventEndermenFromGriefing = true;

	@Comment public static Comment DESC_preventEndermenFromAttackingFirst;
	@Entry public static boolean preventEndermenFromAttackingFirst = true;
}