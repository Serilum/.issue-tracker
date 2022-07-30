/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.36.
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
