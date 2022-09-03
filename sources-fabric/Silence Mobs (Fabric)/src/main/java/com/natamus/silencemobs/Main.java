/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.silencemobs;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.silencemobs.cmds.CommandSt;
import com.natamus.silencemobs.config.ConfigHandler;
import com.natamus.silencemobs.events.EntityEvent;
import com.natamus.silencemobs.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandSt.register(dispatcher);
        });
		
		CollectiveEntityEvents.ON_LIVING_ATTACK.register((Level world, Entity entity, DamageSource damageSource, float damageAmount) -> {
			return EntityEvent.onEntityDamage(world, entity, damageSource, damageAmount);
		});
	}
}
