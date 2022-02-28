/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 4.12.
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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public class CollectiveSpawnEvents {
	private CollectiveSpawnEvents() { }
	 
    public static final Event<CollectiveSpawnEvents.Mob_Check_Spawn> MOB_CHECK_SPAWN = EventFactory.createArrayBacked(CollectiveSpawnEvents.Mob_Check_Spawn.class, callbacks -> (entity, world, spawnerPos, spawnReason) -> {
        for (CollectiveSpawnEvents.Mob_Check_Spawn callback : callbacks) {
        	if (!callback.onMobCheckSpawn(entity, world, spawnerPos, spawnReason)) {
        		return false;
        	}
        }
        
        return true;
    });
    
	@FunctionalInterface
	public interface Mob_Check_Spawn {
		 boolean onMobCheckSpawn(Mob entity, ServerLevel world, BlockPos spawnerPos, MobSpawnType spawnReason);
	}
}
