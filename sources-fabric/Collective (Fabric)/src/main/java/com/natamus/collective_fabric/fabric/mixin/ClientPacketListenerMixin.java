/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.x, mod version: 4.27.
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

import net.minecraft.network.protocol.game.ClientboundChatPreviewPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveChatEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

@Mixin(value = ClientPacketListener.class, priority = 1001)
public class ClientPacketListenerMixin {
	@Final @Shadow private Minecraft minecraft;
	
	@Inject(method = "handleChatPreview(Lnet/minecraft/network/protocol/game/ClientboundChatPreviewPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/chat/ClientChatPreview;handleResponse(ILnet/minecraft/network/chat/Component;)V"), cancellable = true)
	public void ClientPacketListener_handleChatPreview(ClientboundChatPreviewPacket clientboundChatPreviewPacket, CallbackInfo ci) {
		Component message = clientboundChatPreviewPacket.preview();
		Component newMessage = CollectiveChatEvents.CLIENT_CHAT_RECEIVED.invoker().onClientChat(clientboundChatPreviewPacket.queryId(), message, null);
		if (message != newMessage) {
			minecraft.gui.getChat().getFocusedChat().getChatPreview().handleResponse(clientboundChatPreviewPacket.queryId(), newMessage);
			ci.cancel();
		}
	}
}
