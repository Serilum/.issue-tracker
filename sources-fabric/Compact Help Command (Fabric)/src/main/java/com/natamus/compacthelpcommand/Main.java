/*
 * This is the latest source code of Compact Help Command.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.compacthelpcommand;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.compacthelpcommand.cmds.CommandHelp;
import com.natamus.compacthelpcommand.config.ConfigHandler;
import com.natamus.compacthelpcommand.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandHelp.register(dispatcher);
		});
	}
}
