/*
 * This is the latest source code of Full Brightness Toggle.
 * Minecraft version: 1.19.2, mod version: 1.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.fullbrightnesstoggle;

import com.mojang.blaze3d.platform.InputConstants;
import com.natamus.fullbrightnesstoggle.events.ToggleEvent;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class ClientMain implements ClientModInitializer {
	private static KeyMapping hotkey = KeyBindingHelper.registerKeyBinding(new KeyMapping("Toggle Brightness", InputConstants.Type.KEYSYM, 71, "key.categories.misc"));
	
    @Override
    public void onInitializeClient() {		
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		    while (hotkey.isDown()) {
		    	ToggleEvent.onHotkeyPress();
		    	hotkey.setDown(false);
		    }
		});  	
    }
}
