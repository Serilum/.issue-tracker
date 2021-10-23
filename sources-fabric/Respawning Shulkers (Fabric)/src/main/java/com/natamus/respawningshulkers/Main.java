/*
 * This is the latest source code of Respawning Shulkers.
 * Minecraft version: 1.17.x, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Respawning Shulkers ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.respawningshulkers;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.respawningshulkers.config.ConfigHandler;
import com.natamus.respawningshulkers.events.ShulkerEvent;
import com.natamus.respawningshulkers.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			ShulkerEvent.onWorldLoad(world);
		});
		
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			ShulkerEvent.onWorldTick(world);
		});
		
		CollectiveEntityEvents.LIVING_ENTITY_DEATH.register((Level world, Entity entity, DamageSource source) -> {
			ShulkerEvent.onShulkerDeath(world, entity, source);
		});
		
		ServerLifecycleEvents.SERVER_STOPPING.register((MinecraftServer server) -> {
			ShulkerEvent.onServerShutdown(server);
		});
	}
}
