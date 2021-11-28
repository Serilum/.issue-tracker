/*
 * This is the latest source code of Starter Kit.
 * Minecraft version: 1.17.x, mod version: 3.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Starter Kit ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.starterkit;

import com.mojang.brigadier.ParseResults;
import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveCommandEvents;
import com.natamus.starterkit.cmds.CommandStarterkit;
import com.natamus.starterkit.config.ConfigHandler;
import com.natamus.starterkit.events.FirstSpawnEvent;
import com.natamus.starterkit.util.Reference;
import com.natamus.starterkit.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		ConfigHandler.setup();
		
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
    	try {
			Util.getOrCreateGearConfig(true);
		} catch (Exception e) { }
		
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            CommandStarterkit.register(dispatcher);
        });
		
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			FirstSpawnEvent.onSpawn(world, entity);
		});
		
		CollectiveCommandEvents.ON_COMMAND_PARSE.register((String string, ParseResults<CommandSourceStack> parse) -> {
			FirstSpawnEvent.onCommand(string, parse);
		});
	}
}
