/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.x, mod version: 4.25.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.fabric.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;

public class CollectiveWorldEvents {
	private CollectiveWorldEvents() { }
	 
    public static final Event<CollectiveWorldEvents.World_Unload> WORLD_UNLOAD = EventFactory.createArrayBacked(CollectiveWorldEvents.World_Unload.class, callbacks -> (world) -> {
        for (CollectiveWorldEvents.World_Unload callback : callbacks) {
        	callback.onWorldUnload(world);
        }
    });
    
	@FunctionalInterface
	public interface World_Unload {
		 void onWorldUnload(ServerLevel world);
	}
}
