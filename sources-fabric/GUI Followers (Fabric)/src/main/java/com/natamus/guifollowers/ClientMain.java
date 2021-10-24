/*
 * This is the latest source code of GUI Followers.
 * Minecraft version: 1.17.x, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of GUI Followers ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.guifollowers;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.guifollowers.events.FollowerEvent;
import com.natamus.guifollowers.events.GUIEvent;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientMain implements ClientModInitializer {
	private static KeyMapping hotkey = KeyBindingHelper.registerKeyBinding(new KeyMapping("Clear Follower List", InputConstants.Type.KEYSYM, 92, "key.categories.misc"));
	
	@Override
	public void onInitializeClient() { 
		registerEvents();
	}
	
	private void registerEvents() {
		ClientTickEvents.END_CLIENT_TICK.register((Minecraft client) -> {
		    while (hotkey.isDown()) {
		    	FollowerEvent.onHotkeyPress();
		    	hotkey.setDown(false);
		    }
		    
		    FollowerEvent.onPlayerTick(client);
		});
		
		CollectivePlayerEvents.PLAYER_LOGGED_OUT.register((Level world, Player player) -> {
			FollowerEvent.onPlayerLogout(world, player);
		});
		
		HudRenderCallback.EVENT.register((PoseStack poseStack, float tickDelta) -> {
			GUIEvent.renderOverlay(poseStack, tickDelta);
		});
	}
}
