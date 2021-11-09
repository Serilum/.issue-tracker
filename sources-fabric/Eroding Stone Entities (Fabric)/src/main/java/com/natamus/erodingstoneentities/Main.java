/*
 * This is the latest source code of Eroding Stone Entities.
 * Minecraft version: 1.17.x, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Eroding Stone Entities ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
