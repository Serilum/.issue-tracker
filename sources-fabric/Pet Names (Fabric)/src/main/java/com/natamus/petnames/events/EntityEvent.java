/*
 * This is the latest source code of Pet Names.
 * Minecraft version: 1.19.3, mod version: 2.4.
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
		
		EntityFunctions.nameEntity(offspring, StringFunctions.getRandomName(ConfigHandler._useMaleNames, ConfigHandler._useFemaleNames));
	}
}
