/*
 * This is the latest source code of Bouncier Beds.
 * Minecraft version: 1.19.2, mod version: 1.6.
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

package com.natamus.bouncierbeds;

import com.natamus.bouncierbeds.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
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
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

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
