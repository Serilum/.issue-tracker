/*
 * This is the latest source code of Beautified Chat Server.
 * Minecraft version: 1.19.1, mod version: 1.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
