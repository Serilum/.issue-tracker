/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.41.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveMinecraftServerEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveWorldEvents;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;

@Mixin(value = MinecraftServer.class, priority = 1001)
public class MinecraftServerMixin {
	@Inject(method = "setInitialSpawn", at = @At(value = "RETURN"))
	private static void MinecraftServer_setInitialSpawn(ServerLevel serverLevel, ServerLevelData serverLevelData, boolean bl, boolean bl2, CallbackInfo ci) {
		CollectiveMinecraftServerEvents.WORLD_SET_SPAWN.invoker().onSetSpawn(serverLevel, serverLevelData);
	}
	
	@ModifyVariable(method = "stopServer", at = @At(value= "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;close()V", ordinal = 0))
	public ServerLevel MinecraftServer_stopServer(ServerLevel serverlevel1) {
		CollectiveWorldEvents.WORLD_UNLOAD.invoker().onWorldUnload(serverlevel1);
		return serverlevel1;
	}
}
