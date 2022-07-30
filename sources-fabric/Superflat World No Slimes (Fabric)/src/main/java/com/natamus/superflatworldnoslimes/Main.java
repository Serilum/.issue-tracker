/*
 * This is the latest source code of Superflat World No Slimes.
 * Minecraft version: 1.19.1, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Superflat World No Slimes ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.superflatworldnoslimes;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.superflatworldnoslimes.events.SlimeEvent;
import com.natamus.superflatworldnoslimes.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveEntityEvents.PRE_ENTITY_JOIN_WORLD.register((Level world, Entity entity) -> {
			return SlimeEvent.onWorldJoin(world, entity); 
		});
	}
}
