/*
 * This is the latest source code of Beautified Chat Server.
 * Minecraft version: 1.18.x, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Beautified Chat Server ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
		String timestamp = new SimpleDateFormat(ConfigHandler.timestampFormat.getValue()).format(new Date());
		
		String user = serverPlayer.getName().getString();
		String message = messageComponent.getString();
		if (!message.contains(user)) {
			return null;
		}
		
		if (message.contains("> ")) {
			message = message.substring(message.split("> ")[0].length() + 2);
		}
		
		TextComponent output = new TextComponent("");
		String raw_outputstring = ConfigHandler.chatMessageFormat.getValue();
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
