/*
 * This is the latest source code of Vanilla Zoom.
 * Minecraft version: 1.19.3, mod version: 1.2.
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

package com.natamus.vanillazoom;

import com.natamus.vanillazoom.events.ZoomEvent;
import com.natamus.vanillazoom.util.Variables;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class ClientMain implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Variables.hotkey = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.vanillazoom.togglezoom.desc", 342, "key.categories.misc"));

		registerEvents();
	}
	
	private void registerEvents() {
		ClientTickEvents.START_CLIENT_TICK.register((Minecraft mc) -> {
			ZoomEvent.onClientTick();
		});

		UseItemCallback.EVENT.register((player, world, hand) -> {
			return ZoomEvent.onItemUse(player, world, hand);
		});

		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return ZoomEvent.onEntityInteract(player, world, hand, entity, hitResult);
		});
	}
}
