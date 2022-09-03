/*
 * This is the latest source code of Husk Spawn.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.huskspawn;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.objects.SAMObject;
import com.natamus.huskspawn.config.ConfigHandler;
import com.natamus.huskspawn.util.Reference;

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
    	new SAMObject(EntityType.ZOMBIE, EntityType.HUSK, null, ConfigHandler.chanceZombieIsHusk.getValue(), false, false, false);	
	}
}
