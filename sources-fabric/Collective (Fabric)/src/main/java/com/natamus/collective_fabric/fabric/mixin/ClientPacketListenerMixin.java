/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.41.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
