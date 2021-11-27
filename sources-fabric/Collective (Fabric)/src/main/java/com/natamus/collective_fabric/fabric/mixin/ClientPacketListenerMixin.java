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

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveChatEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundChatPacket;

@Mixin(value = ClientPacketListener.class, priority = 1001)
public class ClientPacketListenerMixin {
	@Shadow private Minecraft minecraft;
	
	@Inject(method = "handleChat(Lnet/minecraft/network/protocol/game/ClientboundChatPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;handleChat(Lnet/minecraft/network/chat/ChatType;Lnet/minecraft/network/chat/Component;Ljava/util/UUID;)V"), cancellable = true)
	public void ClientPacketListener_handleChat(ClientboundChatPacket clientboundChatPacket, CallbackInfo ci) {
		Component message = clientboundChatPacket.getMessage();
		Component newMessage = CollectiveChatEvents.CLIENT_CHAT_RECEIVED.invoker().onClientChat(clientboundChatPacket.getType(), message, clientboundChatPacket.getSender());
		if (message != newMessage) {
			minecraft.gui.handleChat(clientboundChatPacket.getType(), newMessage, clientboundChatPacket.getSender());
			ci.cancel();
		}
	}
}
