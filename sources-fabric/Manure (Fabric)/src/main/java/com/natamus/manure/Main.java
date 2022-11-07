/*
 * This is the latest source code of Manure.
 * Minecraft version: 1.19.2, mod version: 1.0.
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

package com.natamus.manure;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.config.DuskConfig;
import com.natamus.manure.config.ConfigHandler;
import com.natamus.manure.dispenser.RecipeManager;
import com.natamus.manure.events.ManureDropEvent;
import com.natamus.manure.items.ManureItems;
import com.natamus.manure.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

		registerEvents();
		registerItems();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		RecipeManager.initDispenserBehavior();

		ServerTickEvents.END_SERVER_TICK.register((MinecraftServer minecraftServer) -> {
			ManureDropEvent.onServerTick(minecraftServer);
		});

		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			ManureDropEvent.onEntityJoin(entity, world);
		});

		ServerEntityEvents.ENTITY_UNLOAD.register((Entity entity, ServerLevel world) -> {
			ManureDropEvent.onEntityLeave(entity, world);
		});
	}

	private void registerItems() {
		Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, "manure"), ManureItems.MANURE);
	}
}
