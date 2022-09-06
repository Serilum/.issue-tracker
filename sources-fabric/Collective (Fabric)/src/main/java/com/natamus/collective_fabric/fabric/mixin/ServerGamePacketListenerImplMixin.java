/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.52.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.collective_fabric.fabric.mixin;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001)
public abstract class ServerGamePacketListenerImplMixin {
	/*@Shadow private @Final MinecraftServer server;
	@Shadow public ServerPlayer player;
	@Shadow private int chatSpamTickCount;
	@Shadow public void disconnect(Component component) { }
	
	@Inject(method = "broadcastChatMessage(Lnet/minecraft/server/network/FilteredText;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastChatMessage(Lnet/minecraft/server/network/FilteredText;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/resources/ResourceKey;)V"), cancellable = true)
	private void ServerGamePacketListenerImpl_broadcastChatMessage(FilteredText<PlayerChatMessage> filteredText, CallbackInfo ci) {
		Pair<Boolean, Component> pair = CollectiveChatEvents.SERVER_CHAT_RECEIVED.invoker().onServerChat(player, filteredText.raw().serverContent(), player.getUUID());
		if (pair != null) {
			Component newMessage = pair.getSecond();

			server.getPlayerList().broadcastChatMessage(new FilteredText<PlayerChatMessage>(new PlayerChatMessage(newMessage, null, null), null), this.player, ChatType.CHAT);
			
			this.chatSpamTickCount += 20;
			if (this.chatSpamTickCount > 200 && !this.server.getPlayerList().isOp(this.player.getGameProfile())) {
				this.disconnect(Component.translatable("disconnect.spam"));
			}
			ci.cancel();
		}
	}*/
}
