/*
 * This is the latest source code of Grass Seeds.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Grass Seeds ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.grassseeds;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.grassseeds.events.GrassEvent;
import com.natamus.grassseeds.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return GrassEvent.onDirtClick(player, world, hand, hitResult);
		});
	}
}
