/*
 * This is the latest source code of Welcome Message.
 * Minecraft version: 1.19.3, mod version: 1.9.
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

package com.natamus.welcomemessage.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_onlyRunOnDedicatedServers;
	@Entry public static boolean onlyRunOnDedicatedServers = true;

	@Comment public static Comment DESC_sendEmptyLineBeforeFirstMessage;
	@Entry public static boolean sendEmptyLineBeforeFirstMessage = true;

	@Comment public static Comment DESC_messageOneText;
	@Entry public static String messageOneText = "Welcome to the server!";

	@Comment public static Comment DESC_messageOneColourIndex;
	@Entry public static int messageOneColourIndex = 2;
	@Comment public static Comment RANGE_messageOneColourIndex;

	@Comment public static Comment DESC_messageOneOptionalURL;
	@Entry public static String messageOneOptionalURL = "";

	@Comment public static Comment DESC_messageTwoText;
	@Entry public static String messageTwoText = "Downloaded from CurseForge! This is a clickable link.";

	@Comment public static Comment DESC_messageTwoColourIndex;
	@Entry public static int messageTwoColourIndex = 14;
	@Comment public static Comment RANGE_messageTwoColourIndex;

	@Comment public static Comment DESC_messageTwoOptionalURL;
	@Entry public static String messageTwoOptionalURL = "https://curseforge.com/members/serilum/projects";

	@Comment public static Comment DESC_messageThreeText;
	@Entry public static String messageThreeText = "You should probably edit this in the config :)";

	@Comment public static Comment DESC_messageThreeColourIndex;
	@Entry public static int messageThreeColourIndex = 15;
	@Comment public static Comment RANGE_messageThreeColourIndex;

	@Comment public static Comment DESC_messageThreeOptionalURL;
	@Entry public static String messageThreeOptionalURL = "";
}