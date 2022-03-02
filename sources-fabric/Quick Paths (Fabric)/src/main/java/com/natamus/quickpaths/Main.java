/*
 * This is the latest source code of Quick Paths.
 * Minecraft version: 1.19.x, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Quick Paths ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.quickpaths;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.quickpaths.events.PathEvent;
import com.natamus.quickpaths.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.server.MinecraftServer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerTickEvents.START_SERVER_TICK.register((MinecraftServer server) -> {
			PathEvent.onServerTick(server);
		});
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return PathEvent.onRightClickGrass(player, world, hand, hitResult);
		});
	}
}
