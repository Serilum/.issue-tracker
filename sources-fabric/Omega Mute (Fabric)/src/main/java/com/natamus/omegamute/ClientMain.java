/*
 * This is the latest source code of Omega Mute.
 * Minecraft version: 1.19.1, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Omega Mute ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
