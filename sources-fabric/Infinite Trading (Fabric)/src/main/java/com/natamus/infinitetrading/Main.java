/*
 * This is the latest source code of Infinite Trading.
 * Minecraft version: 1.19.1, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.infinitetrading;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.infinitetrading.config.ConfigHandler;
import com.natamus.infinitetrading.util.Reference;

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
