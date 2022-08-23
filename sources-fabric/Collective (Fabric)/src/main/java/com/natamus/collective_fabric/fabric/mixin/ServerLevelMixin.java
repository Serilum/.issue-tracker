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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

@Mixin(value = ServerLevel.class, priority = 1001)
public class ServerLevelMixin {
	@Inject(method = "addEntity(Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "HEAD"), cancellable = true)
	private void serverLevel_addEntity(Entity entity, CallbackInfoReturnable<Boolean> ci) {
		Level world = entity.getCommandSenderWorld();
		if (!CollectiveEntityEvents.PRE_ENTITY_JOIN_WORLD.invoker().onPreSpawn(world, entity)) {
			ci.setReturnValue(false);
		}
	}
}
