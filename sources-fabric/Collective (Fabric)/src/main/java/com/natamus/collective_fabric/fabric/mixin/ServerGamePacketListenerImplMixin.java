/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.62.
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

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveChatEvents;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001)
public abstract class ServerGamePacketListenerImplMixin {
	@Shadow private @Final MinecraftServer server;
	@Shadow public ServerPlayer player;
	@Shadow private int chatSpamTickCount;
	@Shadow public void disconnect(Component component) { }
	
	@Inject(method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerList()Lnet/minecraft/server/players/PlayerList;", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private void ServerGamePacketListenerImpl_handleChat(TextFilter.FilteredText filteredText, CallbackInfo ci, String string, Component component, Component component2) {
		Component newMessage = CollectiveChatEvents.SERVER_CHAT_RECEIVED.invoker().onServerChat(player, component2, player.getUUID());
		if (component != newMessage) {
			server.getPlayerList().broadcastMessage(newMessage, (player) -> {
				return this.player.shouldFilterMessageTo(player) ? component : newMessage;
			}, ChatType.CHAT, player.getUUID());
			
			this.chatSpamTickCount += 20;
			if (this.chatSpamTickCount > 200 && !this.server.getPlayerList().isOp(this.player.getGameProfile())) {
				this.disconnect(new TranslatableComponent("disconnect.spam"));
			}
			ci.cancel();
		}
	}
}
