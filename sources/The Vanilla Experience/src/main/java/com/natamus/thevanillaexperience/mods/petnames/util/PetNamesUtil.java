/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
