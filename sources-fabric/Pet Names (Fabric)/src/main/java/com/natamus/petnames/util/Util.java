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

package com.natamus.petnames.util;

import com.natamus.petnames.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;

public class Util {
	public static boolean isNamable(Entity entity) {
		if (entity instanceof Wolf) {
			if (!ConfigHandler.nameWolves.getValue()) {
				return false;
			}
		}
		else if (entity instanceof Cat) {
			if (!ConfigHandler.nameCats.getValue()) {
				return false;
			}
		}
		else if (entity instanceof Horse) {
			if (!ConfigHandler.nameHorses.getValue()) {
				return false;
			}
		}
		else if (entity instanceof Donkey) {
			if (!ConfigHandler.nameDonkeys.getValue()) {
				return false;
			}
		}
		else if (entity instanceof Mule) {
			if (!ConfigHandler.nameMules.getValue()) {
				return false;
			}
		}
		else if (entity instanceof Llama) {
			if (!ConfigHandler.nameLlamas.getValue()) {
				return false;
			}
		}
		else {
			return false;
		}
		return true;
	}
}
