/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.44.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
