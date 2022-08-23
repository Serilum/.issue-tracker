/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.41.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.fabric.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

public class CollectiveSoundEvents {
	private CollectiveSoundEvents() { }
	 
    public static final Event<CollectiveSoundEvents.Sound_Play> SOUND_PLAY = EventFactory.createArrayBacked(CollectiveSoundEvents.Sound_Play.class, callbacks -> (soundEngine, soundInstance) -> {
        for (CollectiveSoundEvents.Sound_Play callback : callbacks) {
        	if (!callback.onSoundPlay(soundEngine, soundInstance)) {
        		return false;
        	}
        }
        
        return true;
    });
    
	@FunctionalInterface
	public interface Sound_Play {
		 boolean onSoundPlay(SoundEngine soundEngine, SoundInstance soundInstance);
	}
}
