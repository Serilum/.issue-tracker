/*
 * This is the latest source code of Recipe Commands.
 * Minecraft version: 1.18.x, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Recipe Commands ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.recipecommands;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.recipecommands.cmds.CommandRecipes;
import com.natamus.recipecommands.util.Recipes;
import com.natamus.recipecommands.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		Recipes.InitRecipes();
		
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			CommandRecipes.register(dispatcher);
		});
	}
}
