/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.41.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.config.CollectiveConfigHandler;
import com.natamus.collective_fabric.events.CollectiveEvents;
import com.natamus.collective_fabric.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class CollectiveFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		System.out.println("Loading Collective (Fabric).");
		
		CollectiveConfigHandler.setup();
		
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			CollectiveEvents.onWorldLoad(world);
		});
		
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			CollectiveEvents.onWorldTick(world);
		});
		
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			CollectiveEvents.onEntityJoinLevel(world, entity);
		});
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
}
