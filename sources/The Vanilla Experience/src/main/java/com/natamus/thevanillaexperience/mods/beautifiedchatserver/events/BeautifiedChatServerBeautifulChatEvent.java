/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.beautifiedchatserver.events;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.natamus.thevanillaexperience.mods.beautifiedchatserver.config.BeautifiedChatServerConfigHandler;
import com.natamus.thevanillaexperience.mods.beautifiedchatserver.util.BeautifiedChatServerUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BeautifiedChatServerBeautifulChatEvent {
	@SubscribeEvent
	public void onServerChat(ServerChatEvent e) {
		String timestamp = new SimpleDateFormat(BeautifiedChatServerConfigHandler.GENERAL.timestampFormat.get()).format(new Date());
		
		String user = e.getUsername();
		String message = e.getMessage();
		
		TextComponent output = new TextComponent("");
		String raw_outputstring = BeautifiedChatServerConfigHandler.GENERAL.chatMessageFormat.get();
		for (String word : raw_outputstring.split("%")) {
			ChatFormatting colour = BeautifiedChatServerUtil.getColour(word);
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

		e.setComponent(output);
	}
}
