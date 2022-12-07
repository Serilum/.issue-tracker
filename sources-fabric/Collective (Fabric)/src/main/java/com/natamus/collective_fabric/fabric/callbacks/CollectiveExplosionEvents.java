/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.25.
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class CollectiveExplosionEvents {
	private CollectiveExplosionEvents() { }
	 
    public static final Event<CollectiveExplosionEvents.Explosion_Detonate> EXPLOSION_DETONATE = EventFactory.createArrayBacked(CollectiveExplosionEvents.Explosion_Detonate.class, callbacks -> (world, sourceEntity, explosion) -> {
        for (CollectiveExplosionEvents.Explosion_Detonate callback : callbacks) {
        	callback.onDetonate(world, sourceEntity, explosion);
        }
    });
    
	@FunctionalInterface
	public interface Explosion_Detonate {
		 void onDetonate(Level world, Entity sourceEntity, Explosion explosion);
	}
}
