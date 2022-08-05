/*
 * This is the latest source code of Eroding Stone Entities.
 * Minecraft version: 1.19.2, mod version: 2.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.erodingstoneentities;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.erodingstoneentities.config.ConfigHandler;
import com.natamus.erodingstoneentities.events.EntityEvent;
import com.natamus.erodingstoneentities.util.Reference;
import com.natamus.erodingstoneentities.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		if (Util.populateArrays()) {
			ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
				EntityEvent.onWorldTick(world);
			});
			
			ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
				EntityEvent.onEntityJoin(world, entity);
			});
		}
	}
}
