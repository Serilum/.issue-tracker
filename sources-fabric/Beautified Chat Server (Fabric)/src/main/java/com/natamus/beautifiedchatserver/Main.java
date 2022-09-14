/*
 * This is the latest source code of Beautified Chat Server.
 * Minecraft version: 1.19.2, mod version: 1.2.
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

package com.natamus.beautifiedchatserver;

import java.util.UUID;

import com.natamus.beautifiedchatserver.config.ConfigHandler;
import com.natamus.beautifiedchatserver.events.BeautifulChatEvent;
import com.natamus.beautifiedchatserver.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveChatEvents;

import net.fabricmc.api.ModInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveChatEvents.SERVER_CHAT_RECEIVED.register((ServerPlayer serverPlayer, Component message, UUID uuid) -> {
			return BeautifulChatEvent.onServerChat(serverPlayer, message, uuid);
		});
	}
}
