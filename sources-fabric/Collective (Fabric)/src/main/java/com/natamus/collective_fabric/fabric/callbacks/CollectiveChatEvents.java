/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.22.
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

package com.natamus.collective_fabric.fabric.callbacks;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class CollectiveChatEvents {
	private CollectiveChatEvents() { }
	 
    public static final Event<CollectiveChatEvents.On_Client_Chat> CLIENT_CHAT_RECEIVED = EventFactory.createArrayBacked(CollectiveChatEvents.On_Client_Chat.class, callbacks -> (chatType, message, senderUUID) -> {
        for (CollectiveChatEvents.On_Client_Chat callback : callbacks) {
        	Component newMessage = callback.onClientChat(chatType, message, senderUUID);
        	if (newMessage != message) {
        		return newMessage;
        	}
        }
        
        return message;
    });
    
    public static final Event<CollectiveChatEvents.On_Server_Chat> SERVER_CHAT_RECEIVED = EventFactory.createArrayBacked(CollectiveChatEvents.On_Server_Chat.class, callbacks -> (serverPlayer, message, senderUUID) -> {
        for (CollectiveChatEvents.On_Server_Chat callback : callbacks) {
        	Pair<Boolean, Component> pair = callback.onServerChat(serverPlayer, message, senderUUID);
        	if (pair != null) {
	        	if (pair.getFirst()) {
	        		return pair;
	        	}
        	}
        }
        
        return null;
    });
    
	@FunctionalInterface
	public interface On_Client_Chat {
		 Component onClientChat(ChatType.BoundNetwork chatType, Component message, UUID senderUUID);
	}
	
	@FunctionalInterface
	public interface On_Server_Chat {
		 Pair<Boolean, Component> onServerChat(ServerPlayer serverPlayer, Component message, UUID senderUUID);
	}
}
