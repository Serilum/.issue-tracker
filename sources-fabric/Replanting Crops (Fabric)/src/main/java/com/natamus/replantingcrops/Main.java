/*
 * This is the latest source code of Replanting Crops.
 * Minecraft version: 1.17.x, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Replanting Crops ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.replantingcrops;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.replantingcrops.config.ConfigHandler;
import com.natamus.replantingcrops.events.CropEvent;
import com.natamus.replantingcrops.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
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
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			return CropEvent.onHarvest(world, player, pos, state, entity);
		});
		
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			CropEvent.onHarvest(world, entity);
		});
	}
}
