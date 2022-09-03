/*
 * This is the latest source code of Pet Names.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.petnames;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveAnimalEvents;
import com.natamus.petnames.config.ConfigHandler;
import com.natamus.petnames.events.EntityEvent;
import com.natamus.petnames.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveAnimalEvents.PRE_BABY_SPAWN.register((ServerLevel world, Animal parentA, Animal parentB, AgeableMob offspring) -> {
			EntityEvent.onBaby(world, parentA, parentB, offspring);
			return true;
		});
	}
}
