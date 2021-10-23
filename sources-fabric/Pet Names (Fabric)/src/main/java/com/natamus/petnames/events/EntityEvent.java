/*
 * This is the latest source code of Pet Names.
 * Minecraft version: 1.17.x, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Pet Names ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
		
		EntityFunctions.nameEntity(offspring, StringFunctions.getRandomName(ConfigHandler._useMaleNames.getValue(), ConfigHandler._useFemaleNames.getValue()));
	}
}
