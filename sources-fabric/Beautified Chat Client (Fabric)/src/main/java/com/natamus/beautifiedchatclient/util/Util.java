/*
 * This is the latest source code of Beautified Chat Client.
 * Minecraft version: 1.19.2, mod version: 1.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
