/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.beautifiedchatserver.util;

import com.natamus.thevanillaexperience.mods.beautifiedchatserver.config.BeautifiedChatServerConfigHandler;

import net.minecraft.ChatFormatting;

public class BeautifiedChatServerUtil {
	public static ChatFormatting getColour(String word) {
		ChatFormatting colour = ChatFormatting.getById(BeautifiedChatServerConfigHandler.GENERAL.chatOtherSymbolsColour.get());
		if (word.equalsIgnoreCase("timestamp")) {
			colour = ChatFormatting.getById(BeautifiedChatServerConfigHandler.GENERAL.chatTimestampColour.get());
		}
		else if (word.equalsIgnoreCase("username")) {
			colour = ChatFormatting.getById(BeautifiedChatServerConfigHandler.GENERAL.chatUsernameColour.get());
		}
		else if (word.equalsIgnoreCase("chatmessage")) {
			colour = ChatFormatting.getById(BeautifiedChatServerConfigHandler.GENERAL.chatMessageColour.get());
		}
		
		return colour;
	}
}
