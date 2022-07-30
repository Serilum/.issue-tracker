/*
 * This is the latest source code of Better Conduit Placement.
 * Minecraft version: 1.19.1, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Better Conduit Placement ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.betterconduitplacement;

import com.natamus.betterconduitplacement.config.ConfigHandler;
import com.natamus.betterconduitplacement.events.ConduitEvent;
import com.natamus.betterconduitplacement.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			return ConduitEvent.onWaterClick(player, world, hand);
		});
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return ConduitEvent.onConduitClick(player, world, hand, hitResult);
		});
		
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			ConduitEvent.onBlockBreak(world, player, pos, state, entity);
		});
	}
}
