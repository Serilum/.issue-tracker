/*
 * This is the latest source code of Save and Load Inventories.
 * Minecraft version: 1.19.2, mod version: 2.3.
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

package com.natamus.saveandloadinventories;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.saveandloadinventories.cmds.CommandListinventories;
import com.natamus.saveandloadinventories.cmds.CommandLoadinventory;
import com.natamus.saveandloadinventories.cmds.CommandSaveinventory;
import com.natamus.saveandloadinventories.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandListinventories.register(dispatcher);
			CommandLoadinventory.register(dispatcher);
			CommandSaveinventory.register(dispatcher);
		});
	}
}
