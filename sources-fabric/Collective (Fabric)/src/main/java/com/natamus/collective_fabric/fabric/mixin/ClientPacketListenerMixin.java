/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.15.
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

package com.natamus.collective_fabric.fabric.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ClientPacketListener.class, priority = 1001)
public class ClientPacketListenerMixin {
	/*@Final @Shadow private Minecraft minecraft;
	@Shadow private RegistryAccess.Frozen registryAccess;
	
	@Inject(method = "handlePlayerChat(Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;)V", at = @At(value = "HEAD"), cancellable = true)
	public void ClientPacketListener_handleChatPreview(ClientboundPlayerChatPacket clientboundPlayerChatPacket, CallbackInfo ci) {
		Component message = clientboundPlayerChatPacket.message().serverContent();
		System.out.println("MESSAGE: " + message.getString());
		Component newMessage = CollectiveChatEvents.CLIENT_CHAT_RECEIVED.invoker().onClientChat(clientboundPlayerChatPacket.chatType(), message, null);
		if (message != newMessage) {
			minecraft.gui.getChat().getFocusedChat().getChatPreview().handleResponse(clientboundPlayerChatPacket.chatType().chatType(), newMessage);
			ci.cancel();
		}
	}*/
}
