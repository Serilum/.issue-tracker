/*
 * This is the latest source code of Automatic Doors.
 * Minecraft version: 1.19.1, mod version: 2.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Automatic Doors ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.automaticdoors;

import com.natamus.automaticdoors.config.ConfigHandler;
import com.natamus.automaticdoors.events.DoorEvent;
import com.natamus.automaticdoors.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			DoorEvent.onWorldLoad(world);
		});

		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			DoorEvent.onWorldTick(world);
		});

		CollectivePlayerEvents.PLAYER_TICK.register((ServerLevel world, ServerPlayer player) -> {
			DoorEvent.onPlayerTick(world, player);
		});
	}
}
