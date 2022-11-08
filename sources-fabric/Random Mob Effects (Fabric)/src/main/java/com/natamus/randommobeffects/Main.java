/*
 * This is the latest source code of Random Mob Effects.
 * Minecraft version: 1.19.2, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.randommobeffects;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.randommobeffects.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
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
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

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
