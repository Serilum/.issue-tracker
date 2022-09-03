/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.19.2, mod version: 3.7.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.advancementscreenshot;

import com.natamus.advancementscreenshot.config.ConfigHandler;
import com.natamus.advancementscreenshot.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;

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
