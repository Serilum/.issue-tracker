/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.17.x, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Cycle Paintings ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.cyclepaintings;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.cyclepaintings.events.PaintingEvent;
import com.natamus.cyclepaintings.util.Reference;
import com.natamus.cyclepaintings.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		Util.setPaintings();
		
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return PaintingEvent.onClick(player, world, hand, entity, hitResult);
		});
	}
}
