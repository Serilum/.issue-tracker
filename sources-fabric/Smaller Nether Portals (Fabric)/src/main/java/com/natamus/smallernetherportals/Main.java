/*
 * This is the latest source code of Smaller Nether Portals.
 * Minecraft version: 1.19.2, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.smallernetherportals;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.smallernetherportals.events.PortalEvent;
import com.natamus.smallernetherportals.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectivePlayerEvents.PLAYER_TICK.register((ServerLevel world, ServerPlayer player) -> {
			PortalEvent.onPlayerTick(world, player);
		});
		
		CollectivePlayerEvents.PLAYER_CHANGE_DIMENSION.register((ServerLevel world, ServerPlayer player) -> {
			PortalEvent.onDimensionChange(world, player);
		}); 
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return PortalEvent.onClick(player, world, hand, hitResult);
		});
	}
}
