/*
 * This is the latest source code of Beautified Chat Client.
 * Minecraft version: 1.18.1, mod version: 1.0.
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

package com.natamus.beautifiedchatclient.events;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.natamus.beautifiedchatclient.config.ConfigHandler;
import com.natamus.beautifiedchatclient.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BeautifulChatEvent {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onClientChat(ClientChatReceivedEvent e) {
		String chatInRaw = e.getMessage().getString();
		if (!chatInRaw.startsWith("<") || !chatInRaw.contains("> ")) {
			return;
		}
		
		String[] cirspl = chatInRaw.split("> ");
		if (cirspl.length < 2) {
			return;
		}
		
		String timestamp = new SimpleDateFormat(ConfigHandler.GENERAL.timestampFormat.get()).format(new Date());
		String user = cirspl[0].substring(1);
		String message = chatInRaw.replace(cirspl[0] + "> ", "");
		
		TextComponent output = new TextComponent("");
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
			
			TextComponent wordcomponent = new TextComponent(toappend);
			wordcomponent.withStyle(colour);
			output.append(wordcomponent);
		}

		e.setMessage(output);
	}
}
