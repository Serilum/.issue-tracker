/*
 * This is the latest source code of Pet Names.
 * Minecraft version: 1.19.1, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.petnames.events;

import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.petnames.config.ConfigHandler;
import com.natamus.petnames.util.Util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;

public class EntityEvent {
	public static void onBaby(ServerLevel world, Animal parentA, Animal parentB, AgeableMob offspring) {
		if (!Util.isNamable(offspring)) {
			return;
		}
		
		EntityFunctions.nameEntity(offspring, StringFunctions.getRandomName(ConfigHandler._useMaleNames.getValue(), ConfigHandler._useFemaleNames.getValue()));
	}
}
