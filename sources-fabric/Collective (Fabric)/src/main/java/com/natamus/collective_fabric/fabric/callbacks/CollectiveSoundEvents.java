/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.43.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
