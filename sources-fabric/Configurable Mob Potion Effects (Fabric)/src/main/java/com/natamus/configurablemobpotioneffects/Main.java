/*
 * This is the latest source code of Configurable Mob Potion Effects.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.configurablemobpotioneffects;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.configurablemobpotioneffects.events.MobEffectsEvent;
import com.natamus.configurablemobpotioneffects.util.Reference;
import com.natamus.configurablemobpotioneffects.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
    	try {
			Util.loadMobConfigFile();
		} catch (Exception ex) {
			System.out.println("[Error] Mob Potion Effects (Fabric) error on loading the entity config file. The mod has been disabled.");
			ex.printStackTrace();
			return;
		}
    	
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			MobEffectsEvent.onEntityJoin(world, entity);
		});
    	
		CollectiveEntityEvents.ON_LIVING_ATTACK.register((Level world, Entity entity, DamageSource damageSource, float damageAmount) -> {
			MobEffectsEvent.onEntityDamage(world, entity, damageSource, damageAmount);
			return true;
		});
	}
}
