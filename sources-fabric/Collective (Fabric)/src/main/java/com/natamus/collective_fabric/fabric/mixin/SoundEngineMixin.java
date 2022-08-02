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

import com.natamus.collective_fabric.fabric.callbacks.CollectiveSoundEvents;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

@Mixin(value = SoundEngine.class, priority = 1001)
public class SoundEngineMixin {
	@Inject(method = "play", at = @At(value= "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SoundInstance;canPlaySound()Z", ordinal = 0), cancellable = true)
	public void SoundEngine_play(SoundInstance soundInstance, CallbackInfo ci) {
		SoundEngine soundEngine = (SoundEngine)(Object)this;
		if (!CollectiveSoundEvents.SOUND_PLAY.invoker().onSoundPlay(soundEngine, soundInstance)) {
			ci.cancel();
		}
	}
}
