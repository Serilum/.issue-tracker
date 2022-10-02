/*
 * This is the latest source code of Fire Spread Tweaks.
 * Minecraft version: 1.19.2, mod version: 1.8.
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

package com.natamus.firespreadtweaks;

import java.util.EnumSet;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveWorldEvents;
import com.natamus.firespreadtweaks.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
import com.natamus.firespreadtweaks.events.FireSpreadEvent;
import com.natamus.firespreadtweaks.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			FireSpreadEvent.onWorldTick(world);
		});
		
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			FireSpreadEvent.onWorldLoad(world);
		});
		
		CollectiveWorldEvents.WORLD_UNLOAD.register((ServerLevel world) -> {
			FireSpreadEvent.onWorldUnload(world);
		});
		
		CollectiveBlockEvents.NEIGHBOUR_NOTIFY.register((Level world, BlockPos pos, BlockState state, EnumSet<Direction> notifiedSides, boolean forceRedstoneUpdate) -> {
			FireSpreadEvent.onNeighbourNotice(world, pos, state, notifiedSides, forceRedstoneUpdate);
			return true;
		});
	}
}
