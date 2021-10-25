/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.51.
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

import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;

import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.phys.Vec3;

@Mixin(value = AbstractHorse.class, priority = 1001)
public class AbstractHorseMixin {
	@Inject(method = "travel", at = @At(value= "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;setIsJumping(Z)V", ordinal = 0))
	public void AbstractHorse_travel(Vec3 vec3, CallbackInfo ci) {
		AbstractHorse horse = (AbstractHorse)(Object)this;
		CollectiveEntityEvents.ON_ENTITY_IS_JUMPING.invoker().onLivingJump(horse.level, horse);
	}
}
