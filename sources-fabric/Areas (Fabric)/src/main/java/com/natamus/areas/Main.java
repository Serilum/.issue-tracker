/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.18.x, mod version: 3.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.areas;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.areas.config.ConfigHandler;
import com.natamus.areas.util.Reference;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
 import net.minecraftforge.fml.loading.FMLEnvironment;
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		/*         MinecraftForge.EVENT_BUS.register(this);
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			SheepEvent.onSheepSpawn(world, entity);
		});
		*/
/* 			MinecraftForge.EVENT_BUS.register(new GUIEvent(Minecraft.getInstance()));
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			SheepEvent.onSheepSpawn(world, entity);
		});
		*/
/*     	MinecraftForge.EVENT_BUS.register(new AreaEvent());
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			SheepEvent.onSheepSpawn(world, entity);
		});
		*/
	}
}
