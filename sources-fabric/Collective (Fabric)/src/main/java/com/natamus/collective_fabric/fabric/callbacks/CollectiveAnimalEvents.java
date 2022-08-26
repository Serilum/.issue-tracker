/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.50.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.fabric.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;

public class CollectiveAnimalEvents {
	private CollectiveAnimalEvents() { }
	 
    public static final Event<CollectiveAnimalEvents.On_Baby_Spawn> PRE_BABY_SPAWN = EventFactory.createArrayBacked(CollectiveAnimalEvents.On_Baby_Spawn.class, callbacks -> (world, parentA, parentB, offspring) -> {
        for (CollectiveAnimalEvents.On_Baby_Spawn callback : callbacks) {
        	if (!callback.onBabySpawn(world, parentA, parentB, offspring)) {
        		return false;
        	}
        }
        
        return true;
    });
    
	@FunctionalInterface
	public interface On_Baby_Spawn {
		 boolean onBabySpawn(ServerLevel world, Animal parentA, Animal parentB, AgeableMob offspring);
	}
}
