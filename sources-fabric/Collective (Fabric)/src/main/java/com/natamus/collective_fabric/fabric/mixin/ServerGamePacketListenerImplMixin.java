/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.49.
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

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveChatEvents;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001)
public class ServerGamePacketListenerImplMixin {
	@ModifyVariable(method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;getPlayerList()Lnet/minecraft/server/players/PlayerList;", ordinal = 0), ordinal = 0)
	public Component ServerGamePacketListenerImpl_handleChat(Component component2, TextFilter.FilteredText filteredText) {
		ServerGamePacketListenerImpl listener = (ServerGamePacketListenerImpl)(Object)this;
		ServerPlayer serverPlayer = listener.player;
		
		Component newMessage = CollectiveChatEvents.SERVER_CHAT_RECEIVED.invoker().onServerChat(serverPlayer, component2, serverPlayer.getUUID());
		if (component2 != newMessage) {
			component2 = newMessage;
		}
		
		return component2;
	}
}
