/*
 * This is the latest source code of Shulker Drops Two.
 * Minecraft version: 1.19.2, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.shulkerdropstwo;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.shulkerdropstwo.config.ConfigHandler;
import com.natamus.shulkerdropstwo.events.EntityEvent;
import com.natamus.shulkerdropstwo.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.damagesource.DamageSource;
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
		CollectiveEntityEvents.ON_ENTITY_IS_DROPPING_LOOT.register((Level world, Entity entity, DamageSource damageSource) -> {
			EntityEvent.mobItemDrop(world, entity, damageSource);
		});
	}
}
