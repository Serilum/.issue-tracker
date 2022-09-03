/*
 * This is the latest source code of More Zombie Villagers.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.morezombievillagers;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.objects.SAMObject;
import com.natamus.morezombievillagers.config.ConfigHandler;
import com.natamus.morezombievillagers.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.EntityType;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
    	new SAMObject(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, null, ConfigHandler.zombieIsVillagerChance.getValue(), false, false, false);		
	}
}
