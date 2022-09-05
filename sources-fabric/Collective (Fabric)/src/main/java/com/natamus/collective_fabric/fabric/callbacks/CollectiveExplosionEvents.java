/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.51.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
