/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.50.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
