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

package com.natamus.beautifiedchatclient.events;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.natamus.beautifiedchatclient.config.ConfigHandler;
import com.natamus.beautifiedchatclient.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class BeautifulChatEvent {
	public static Component onClientChat(ChatType type, Component message, UUID senderUUID) {
		String chatInRaw = message.getString();
		if (!chatInRaw.startsWith("<") || !chatInRaw.contains("> ")) {
			return message;
		}
		
		String[] cirspl = chatInRaw.split("> ");
		if (cirspl.length < 2) {
			return message;
		}
		
		String timestamp = new SimpleDateFormat(ConfigHandler.timestampFormat).format(new Date());
		String user = cirspl[0].substring(1);
		String messageString = chatInRaw.replace(cirspl[0] + "> ", "");
		
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
				toappend = messageString;
			}
			
			TextComponent wordcomponent = new TextComponent(toappend);
			wordcomponent.withStyle(colour);
			output.append(wordcomponent);
		}

		return output;
	}
}
