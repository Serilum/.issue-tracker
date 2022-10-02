/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.4.
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
