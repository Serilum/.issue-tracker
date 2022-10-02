/*
 * This is the latest source code of Beautified Chat Client.
 * Minecraft version: 1.18.2, mod version: 1.4.
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

package com.natamus.beautifiedchatclient.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_chatMessageFormat;
	@Entry public static String chatMessageFormat = "%timestamp% | %username%: %chatmessage%";

	@Comment public static Comment DESC_timestampFormat;
	@Entry public static String timestampFormat = "HH:mm";

	@Comment public static Comment DESC_chatTimestampColour;
	@Entry public static int chatTimestampColour = 8;
	@Comment public static Comment RANGE_chatTimestampColour;

	@Comment public static Comment DESC_chatUsernameColour;
	@Entry public static int chatUsernameColour = 2;
	@Comment public static Comment RANGE_chatUsernameColour;

	@Comment public static Comment DESC_chatMessageColour;
	@Entry public static int chatMessageColour = 15;
	@Comment public static Comment RANGE_chatMessageColour;

	@Comment public static Comment DESC_chatOtherSymbolsColour;
	@Entry public static int chatOtherSymbolsColour = 7;
	@Comment public static Comment RANGE_chatOtherSymbolsColour;
}