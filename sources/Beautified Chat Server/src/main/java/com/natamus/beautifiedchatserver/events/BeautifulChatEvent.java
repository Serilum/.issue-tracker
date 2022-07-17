/*
 * This is the latest source code of Beautified Chat Server.
 * Minecraft version: 1.19.0, mod version: 1.3.
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

import com.natamus.beautifiedchatserver.config.ConfigHandler;
import com.natamus.beautifiedchatserver.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.text.SimpleDateFormat;
import java.util.Date;

@EventBusSubscriber
public class BeautifulChatEvent {
	@SubscribeEvent
	public void onServerChat(ServerChatEvent e) {
		String timestamp = new SimpleDateFormat(ConfigHandler.GENERAL.timestampFormat.get()).format(new Date());
		
		String user = e.getUsername();
		String message = e.getMessage();
		
		MutableComponent output = Component.literal("");
		String raw_outputstring = ConfigHandler.GENERAL.chatMessageFormat.get();
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
			
			MutableComponent wordcomponent = Component.literal(toappend);
			wordcomponent.withStyle(colour);
			output.append(wordcomponent);
		}

		e.setComponent(output);
	}
}
