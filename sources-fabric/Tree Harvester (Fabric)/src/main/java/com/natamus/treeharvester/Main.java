/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.19.1, mod version: 5.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Tree Harvester ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.treeharvester;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.treeharvester.config.ConfigHandler;
import com.natamus.treeharvester.events.TreeEvent;
import com.natamus.treeharvester.util.Reference;
import com.natamus.treeharvester.util.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		try {
			Util.setupAxeBlacklist();
		}
		catch(Exception ex) {
			return;
		}

		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			TreeEvent.onWorldLoad(world);
		});

		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			TreeEvent.onWorldTick(world);
		});

		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			return TreeEvent.onTreeHarvest(world, player, pos, state, entity);
		});

		CollectivePlayerEvents.ON_PLAYER_DIG_SPEED_CALC.register((Level world, Player player, float digSpeed, BlockState state) -> {
			return TreeEvent.onHarvestBreakSpeed(world, player, digSpeed, state);
		});
	}
}
