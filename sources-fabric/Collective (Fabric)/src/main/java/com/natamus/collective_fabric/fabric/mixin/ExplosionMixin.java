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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveExplosionEvents;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

@Mixin(value = Explosion.class, priority = 1001)
public class ExplosionMixin {
	@Shadow private Level level;
	@Shadow private Entity source;
	
	@Inject(method = "explode()V", at = @At(value = "TAIL"))
	public void Explosion_explode(CallbackInfo ci) {
		Explosion explosion = (Explosion)(Object)this;
		CollectiveExplosionEvents.EXPLOSION_DETONATE.invoker().onDetonate(level, source, explosion);
	}
}