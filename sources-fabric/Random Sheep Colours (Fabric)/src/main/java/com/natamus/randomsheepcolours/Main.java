/*
 * This is the latest source code of Random Sheep Colours.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.randomsheepcolours;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.randomsheepcolours.config.ConfigHandler;
import com.natamus.randomsheepcolours.events.SheepEvent;
import com.natamus.randomsheepcolours.util.Reference;
import com.natamus.randomsheepcolours.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
     	Util.initColours();
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			SheepEvent.onSheepSpawn(world, entity);
		});
	}
}
