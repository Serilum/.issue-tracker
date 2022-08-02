/*
 * This is the latest source code of Omega Mute.
 * Minecraft version: 1.19.1, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.omegamute;

import com.mojang.blaze3d.platform.InputConstants;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveSoundEvents;
import com.natamus.omegamute.events.MuteEvent;
import com.natamus.omegamute.util.Util;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

public class ClientMain implements ClientModInitializer {
	private static KeyMapping hotkey = KeyBindingHelper.registerKeyBinding(new KeyMapping("Reload Omega Mute config", InputConstants.Type.KEYSYM, 46, "key.categories.misc"));
	
    @Override
    public void onInitializeClient() {		
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		    while (hotkey.isDown()) {
		    	MuteEvent.onHotkeyPress();
		    	hotkey.setDown(false);
		    }
		}); 
		
 		try {
			Util.loadSoundFile();
		} catch (Exception ex) {
			return;
		}
 		
		CollectiveSoundEvents.SOUND_PLAY.register((SoundEngine soundEngine, SoundInstance soundInstance) -> {
			return MuteEvent.onSoundEvent(soundEngine, soundInstance);
		});
    }
}
