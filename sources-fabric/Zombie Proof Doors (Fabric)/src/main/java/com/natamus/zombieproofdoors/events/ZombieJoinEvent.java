/*
 * This is the latest source code of Zombie Proof Doors.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.zombieproofdoors.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class ZombieJoinEvent {
	public static void onEntityJoin(Level world, Entity entity) {
		if (!(entity instanceof Zombie)) {
			return;
		}
		
		Zombie zombie = (Zombie)entity;
		zombie.setCanBreakDoors(false);
	}
}
