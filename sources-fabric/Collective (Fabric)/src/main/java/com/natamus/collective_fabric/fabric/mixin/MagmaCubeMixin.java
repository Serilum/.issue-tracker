/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.36.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;

import net.minecraft.world.entity.monster.MagmaCube;

@Mixin(value = MagmaCube.class, priority = 1001)
public class MagmaCubeMixin {
	@Inject(method = "jumpFromGround", at = @At(value= "TAIL"))
	public void MagmaCube_jumpFromGround(CallbackInfo ci) {
		MagmaCube magmaCube = (MagmaCube)(Object)this;
		CollectiveEntityEvents.ON_ENTITY_IS_JUMPING.invoker().onLivingJump(magmaCube.level, magmaCube);
	}
}
