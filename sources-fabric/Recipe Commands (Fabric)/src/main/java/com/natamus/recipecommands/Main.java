/*
 * This is the latest source code of Recipe Commands.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.recipecommands;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.recipecommands.cmds.CommandRecipes;
import com.natamus.recipecommands.util.Recipes;
import com.natamus.recipecommands.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		Recipes.InitRecipes();
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandRecipes.register(dispatcher);
		});
	}
}
