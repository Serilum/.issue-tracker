/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.25.
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

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001, remap = false)
public abstract class ServerGamePacketListenerImplMixin {
	/*@Shadow private @Final MinecraftServer server;
	@Shadow public ServerPlayer player;
	@Shadow private int chatSpamTickCount;
	@Shadow public void disconnect(Component component) { }

    @Inject(method = "method_45064(Ljava/util/concurrent/CompletableFuture;Ljava/util/concurrent/CompletableFuture;Ljava/lang/Void;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/PlayerChatMessage;filter(Lnet/minecraft/network/chat/FilterMask;)Lnet/minecraft/network/chat/PlayerChatMessage;"), cancellable = true)
    public void handleChat(CompletableFuture completableFuture, CompletableFuture completableFuture2, Void void_, CallbackInfo ci) {
        FilterMask filterMask = ((FilteredText)completableFuture.join()).mask();
        PlayerChatMessage playerChatMessage = ((PlayerChatMessage)completableFuture2.join()).filter(filterMask);
        Component message = Component.literal("<" + this.player.getName().getString() + "> " + playerChatMessage.serverContent().getString());

        Pair<Boolean, Component> pair = CollectiveChatEvents.SERVER_CHAT_RECEIVED.invoker().onServerChat(this.player, message, player.getUUID());
        if (pair != null) {
            Component newMessage = pair.getSecond();

            MessageFunctions.broadcastMessage(this.player.getCommandSenderWorld(), (MutableComponent) newMessage);

            this.chatSpamTickCount += 20;
            if (this.chatSpamTickCount > 200 && !this.server.getPlayerList().isOp(this.player.getGameProfile())) {
                this.disconnect(Component.translatable("disconnect.spam"));
            }
            ci.cancel();
        }
    }*/
}
