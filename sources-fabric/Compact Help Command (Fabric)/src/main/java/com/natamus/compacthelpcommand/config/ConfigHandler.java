/*
 * This is the latest source code of Compact Help Command.
 * Minecraft version: 1.19.2, mod version: 1.7.
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

package com.natamus.compacthelpcommand.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_addVerticalBarSpacing;
	@Entry public static boolean addVerticalBarSpacing = true;

	@Comment public static Comment DESC_amountCommandsPerPage;
	@Entry public static int amountCommandsPerPage = 8;
	@Comment public static Comment RANGE_amountCommandsPerPage;

	@Comment public static Comment DESC_commandColour;
	@Entry public static int commandColour = 2;
	@Comment public static Comment RANGE_commandColour;

	@Comment public static Comment DESC_subcommandColour;
	@Entry public static int subcommandColour = 7;
	@Comment public static Comment RANGE_subcommandColour;
}