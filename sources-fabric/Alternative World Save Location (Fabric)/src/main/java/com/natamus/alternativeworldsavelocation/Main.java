/*
 * This is the latest source code of Alternative World Save Location.
 * Minecraft version: 1.19.1, mod version: 1.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.alternativeworldsavelocation;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.alternativeworldsavelocation.config.ConfigHandler;
import com.natamus.alternativeworldsavelocation.util.Reference;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		
	}
}
