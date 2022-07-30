/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.36.
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
