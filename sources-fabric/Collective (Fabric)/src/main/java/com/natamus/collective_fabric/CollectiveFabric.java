/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.44.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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

public class CollectiveFabric implements ModInitializer { //
	@Override
	public void onInitialize() {
		System.out.println("Loading Collective (Fabric) version " + Reference.VERSION + ".");
		
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
