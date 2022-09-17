/*
 * This is the latest source code of Beautified Chat Server.
 * Minecraft version: 1.19.2, mod version: 1.6.
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

import com.mojang.logging.LogUtils;
import com.natamus.beautifiedchatserver.config.ConfigHandler;
import com.natamus.beautifiedchatserver.util.Util;
import com.natamus.collective.functions.MessageFunctions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

@EventBusSubscriber
public class BeautifulChatEvent {
	private static final Logger logger = LogUtils.getLogger();

	@SubscribeEvent
	public void onServerChat(ServerChatEvent.Submitted e) {
		String timestamp = new SimpleDateFormat(ConfigHandler.GENERAL.timestampFormat.get()).format(new Date());

		ServerPlayer serverplayer = e.getPlayer();
		String user = e.getUsername();
		Component component = e.getMessage();
		String message = component.getString();
		
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

		e.setCanceled(true);
		serverplayer.server.execute(() -> {
			logger.info(output.getString());
			MessageFunctions.broadcastMessage(serverplayer.level, output);
		});
	}
}
