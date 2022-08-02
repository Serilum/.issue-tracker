/*
 * This is the latest source code of Bouncier Beds.
 * Minecraft version: 1.19.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.bouncierbeds;

import com.natamus.bouncierbeds.config.ConfigHandler;
import com.natamus.bouncierbeds.events.EntityEvent;
import com.natamus.bouncierbeds.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveEntityEvents.ON_ENTITY_IS_JUMPING.register((Level world, Entity entity) -> {
			EntityEvent.onLivingJump(world, entity);
		});

		CollectiveEntityEvents.ON_FALL_DAMAGE_CALC.register((Level world, Entity entity, float f, float g) -> {
			return EntityEvent.onFall(world, entity, f, g);
		});
	}
}
