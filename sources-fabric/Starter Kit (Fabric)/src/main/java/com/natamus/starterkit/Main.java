/*
 * This is the latest source code of Starter Kit.
 * Minecraft version: 1.19.2, mod version: 3.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
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
