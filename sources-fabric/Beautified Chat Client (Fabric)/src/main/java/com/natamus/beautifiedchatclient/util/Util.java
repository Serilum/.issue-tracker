/*
 * This is the latest source code of Beautified Chat Client.
 * Minecraft version: 1.17.x, mod version: 1.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Beautified Chat Client ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.beautifiedchatclient.util;

import com.natamus.beautifiedchatclient.config.ConfigHandler;

import net.minecraft.ChatFormatting;

public class Util {
	public static ChatFormatting getColour(String word) {
		ChatFormatting colour = ChatFormatting.getById(ConfigHandler.chatOtherSymbolsColour.getValue());
		if (word.equalsIgnoreCase("timestamp")) {
			colour = ChatFormatting.getById(ConfigHandler.chatTimestampColour.getValue());
		}
		else if (word.equalsIgnoreCase("username")) {
			colour = ChatFormatting.getById(ConfigHandler.chatUsernameColour.getValue());
		}
		else if (word.equalsIgnoreCase("chatmessage")) {
			colour = ChatFormatting.getById(ConfigHandler.chatMessageColour.getValue());
		}
		
		return colour;
	}
}
