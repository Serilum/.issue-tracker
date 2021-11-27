/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 3.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.fabric.callbacks;

import java.util.UUID;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CollectiveChatEvents {
	private CollectiveChatEvents() { }
	 
    public static final Event<CollectiveChatEvents.On_Client_Chat> CLIENT_CHAT_RECEIVED = EventFactory.createArrayBacked(CollectiveChatEvents.On_Client_Chat.class, callbacks -> (type, message, senderUUID) -> {
        for (CollectiveChatEvents.On_Client_Chat callback : callbacks) {
        	Component newMessage = callback.onClientChat(type, message, senderUUID);
        	if (newMessage != message) {
        		return newMessage;
        	}
        }
        
        return message;
    });
    
    public static final Event<CollectiveChatEvents.On_Server_Chat> SERVER_CHAT_RECEIVED = EventFactory.createArrayBacked(CollectiveChatEvents.On_Server_Chat.class, callbacks -> (serverPlayer, message, senderUUID) -> {
        for (CollectiveChatEvents.On_Server_Chat callback : callbacks) {
        	Component newMessage = callback.onServerChat(serverPlayer, message, senderUUID);
        	if (newMessage != message) {
        		return newMessage;
        	}
        }
        
        return message;
    });
    
	@FunctionalInterface
	public interface On_Client_Chat {
		 Component onClientChat(ChatType type, Component message, UUID senderUUID);
	}
	
	@FunctionalInterface
	public interface On_Server_Chat {
		 Component onServerChat(ServerPlayer serverPlayer, Component message, UUID senderUUID);
	}
}
