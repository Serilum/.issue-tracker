/*
 * This is the latest source code of Beautified Chat Server.
 * Minecraft version: 1.18.2, mod version: 1.9.
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

package com.natamus.beautifiedchatserver.events;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.mojang.datafixers.util.Pair;
import com.natamus.beautifiedchatserver.config.ConfigHandler;
import com.natamus.beautifiedchatserver.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class BeautifulChatEvent {
	public static Pair<Boolean, Component> onServerChat(ServerPlayer serverPlayer, Component messageComponent, UUID uuid) {
		String timestamp = new SimpleDateFormat(ConfigHandler.timestampFormat).format(new Date());
		
		String user = serverPlayer.getName().getString();
		String message = messageComponent.getString();
		if (!message.contains(user)) {
			return null;
		}
		
		if (message.contains("> ")) {
			message = message.substring(message.split("> ")[0].length() + 2);
		}
		
		TextComponent output = new TextComponent("");
		String raw_outputstring = ConfigHandler.chatMessageFormat;
		for (String word : raw_outputstring.split("%")) {
			ChatFormatting colour = Util.getColour(word);
			String toappend = word;
			
			if (word.equalsIgnoreCase("timestamp")) {
				toappend = timestamp;
			}
			else if (word.equalsIgnoreCase("username")) {
				toappend = user;
			}
			else if (word.equalsIgnoreCase("chatmessage")) {
				toappend = message;
			}
			
			TextComponent wordcomponent = new TextComponent(toappend);
			wordcomponent.withStyle(colour);
			output.append(wordcomponent);
		}

		return new Pair<Boolean, Component>(true, output);
	}
}
