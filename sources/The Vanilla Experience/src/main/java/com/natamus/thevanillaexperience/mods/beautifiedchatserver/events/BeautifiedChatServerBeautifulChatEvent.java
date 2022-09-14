/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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
