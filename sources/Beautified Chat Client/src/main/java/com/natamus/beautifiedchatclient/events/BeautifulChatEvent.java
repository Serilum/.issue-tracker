/*
 * This is the latest source code of Beautified Chat Client.
 * Minecraft version: 1.19.1, mod version: 1.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.beautifiedchatclient.events;

import com.natamus.beautifiedchatclient.config.ConfigHandler;
import com.natamus.beautifiedchatclient.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.text.SimpleDateFormat;
import java.util.Date;

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

		e.setMessage(output);
	}
}
