/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.44.
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveMinecraftServerEvents;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(method = "setInitialSpawn(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/storage/ServerLevelData;ZZ)V", at = @At(value = "RETURN"))
	private static void MinecraftServer_setInitialSpawn(ServerLevel serverLevel, ServerLevelData serverLevelData, boolean bl, boolean bl2, CallbackInfo ci) {
		CollectiveMinecraftServerEvents.WORLD_SET_SPAWN.invoker().onSetSpawn(serverLevel, serverLevelData);
	}
}
