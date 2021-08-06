/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.petnames.util;

import com.natamus.thevanillaexperience.mods.petnames.config.PetNamesConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;

public class PetNamesUtil {
	public static boolean isNamable(Entity entity) {
		if (entity instanceof Wolf) {
			if (!PetNamesConfigHandler.GENERAL.nameWolves.get()) {
				return false;
			}
		}
		else if (entity instanceof Cat) {
			if (!PetNamesConfigHandler.GENERAL.nameCats.get()) {
				return false;
			}
		}
		else if (entity instanceof Horse) {
			if (!PetNamesConfigHandler.GENERAL.nameHorses.get()) {
				return false;
			}
		}
		else if (entity instanceof Donkey) {
			if (!PetNamesConfigHandler.GENERAL.nameDonkeys.get()) {
				return false;
			}
		}
		else if (entity instanceof Mule) {
			if (!PetNamesConfigHandler.GENERAL.nameMules.get()) {
				return false;
			}
		}
		else if (entity instanceof Llama) {
			if (!PetNamesConfigHandler.GENERAL.nameLlamas.get()) {
				return false;
			}
		}
		else {
			return false;
		}
		return true;
	}
}
