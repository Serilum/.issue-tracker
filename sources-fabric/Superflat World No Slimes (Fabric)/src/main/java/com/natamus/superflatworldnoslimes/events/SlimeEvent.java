/*
 * This is the latest source code of Superflat World No Slimes.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.superflatworldnoslimes.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class SlimeEvent {
	public static boolean onWorldJoin(Level world, Entity entity) {
		if (world.isClientSide) {
			return true;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		
		if (entity instanceof Slime) {
			if (serverworld.getServer().getWorldData().worldGenSettings().isFlatWorld()) {
				return false;
			}
		}
		
		return true;
	}
}
