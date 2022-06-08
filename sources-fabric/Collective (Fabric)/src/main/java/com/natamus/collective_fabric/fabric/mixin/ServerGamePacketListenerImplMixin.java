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

import com.mojang.datafixers.util.Pair;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveChatEvents;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001)
public abstract class ServerGamePacketListenerImplMixin {
	@Shadow private @Final MinecraftServer server;
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
	}
}
