/*
 * This is the latest source code of Better Beacon Placement.
 * Minecraft version: 1.17.x, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Better Beacon Placement ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.betterbeaconplacement;

import com.natamus.betterbeaconplacement.config.ConfigHandler;
import com.natamus.betterbeaconplacement.events.BeaconEvent;
import com.natamus.betterbeaconplacement.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return BeaconEvent.onBeaconClick(player, world, hand, hitResult);
		});
		
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			BeaconEvent.onBlockBreak(world, player, pos, state, entity);
		});
	}
}
