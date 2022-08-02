/*
 * This is the latest source code of Replanting Crops.
 * Minecraft version: 1.19.1, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
