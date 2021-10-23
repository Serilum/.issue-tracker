/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.48.
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.natamus.collective_fabric.util.Reference;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {
	@ModifyVariable(method = "serverTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V", at = @At(value= "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tryAddFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)Z"))
	private Entity BaseSpawner_tick(Entity entity) {
		entity.addTag(Reference.MOD_ID + ".fromspawner");
		return entity;
	}
}
