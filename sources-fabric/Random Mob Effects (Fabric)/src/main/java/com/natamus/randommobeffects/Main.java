/*
 * This is the latest source code of Random Mob Effects.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.randommobeffects;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.randommobeffects.config.ConfigHandler;
import com.natamus.randommobeffects.events.AddEffectEvent;
import com.natamus.randommobeffects.util.Reference;
import com.natamus.randommobeffects.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
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
    	try {
	    	if (Util.setupPotionEffects()) {
	    		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
	    			AddEffectEvent.onMobSpawn(world, entity);
	    		});
	    	}
		} catch (Exception ex) {
			System.out.println("!!! Something went wrong while initializing Random Mob MobEffects. The mod has been disabled.");
			return;
		}
	}
}
