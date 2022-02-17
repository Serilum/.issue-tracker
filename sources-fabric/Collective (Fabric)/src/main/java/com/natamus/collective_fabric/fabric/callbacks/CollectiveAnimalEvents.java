/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 4.5.
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
