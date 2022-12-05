/*
 * This is the latest source code of Starter Kit.
 * Minecraft version: 1.19.2, mod version: 4.0.
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

package com.natamus.starterkit;

import com.mojang.brigadier.ParseResults;
import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveCommandEvents;
import com.natamus.starterkit.cmds.CommandStarterkit;
import com.natamus.starterkit.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
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
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);
		
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
