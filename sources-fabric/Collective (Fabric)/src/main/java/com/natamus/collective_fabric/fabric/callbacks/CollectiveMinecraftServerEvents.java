/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.68.
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

package com.natamus.collective_fabric.fabric.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;

public final class CollectiveMinecraftServerEvents {
	private CollectiveMinecraftServerEvents() { }
		 
    public static final Event<CollectiveMinecraftServerEvents.Set_Spawn> WORLD_SET_SPAWN = EventFactory.createArrayBacked(CollectiveMinecraftServerEvents.Set_Spawn.class, callbacks -> (serverLevel, serverLevelData) -> {
        for (CollectiveMinecraftServerEvents.Set_Spawn callback : callbacks) {
        	callback.onSetSpawn(serverLevel, serverLevelData);
        }
    });
    
	@FunctionalInterface
	public interface Set_Spawn {
		 void onSetSpawn(ServerLevel serverLevel, ServerLevelData serverLevelData);
	}
}
