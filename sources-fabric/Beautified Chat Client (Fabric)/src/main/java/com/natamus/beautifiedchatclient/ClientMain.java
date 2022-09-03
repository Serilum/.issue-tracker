/*
 * This is the latest source code of Beautified Chat Client.
 * Minecraft version: 1.19.2, mod version: 1.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.beautifiedchatclient;

import java.util.UUID;

import com.natamus.beautifiedchatclient.events.BeautifulChatEvent;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveChatEvents;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;

public class ClientMain implements ClientModInitializer {
	@Override
	public void onInitializeClient() { 
		registerEvents();
	}
	
	private void registerEvents() {
		CollectiveChatEvents.CLIENT_CHAT_RECEIVED.register((ChatType type, Component message, UUID senderUUID) -> {
			return BeautifulChatEvent.onClientChat(type, message, senderUUID);
		});
	}
}
