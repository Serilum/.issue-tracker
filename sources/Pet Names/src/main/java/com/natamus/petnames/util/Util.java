/*
 * This is the latest source code of Pet Names.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
			if (!ConfigHandler.GENERAL.nameWolves.get()) {
				return false;
			}
		}
		else if (entity instanceof Cat) {
			if (!ConfigHandler.GENERAL.nameCats.get()) {
				return false;
			}
		}
		else if (entity instanceof Horse) {
			if (!ConfigHandler.GENERAL.nameHorses.get()) {
				return false;
			}
		}
		else if (entity instanceof Donkey) {
			if (!ConfigHandler.GENERAL.nameDonkeys.get()) {
				return false;
			}
		}
		else if (entity instanceof Mule) {
			if (!ConfigHandler.GENERAL.nameMules.get()) {
				return false;
			}
		}
		else if (entity instanceof Llama) {
			if (!ConfigHandler.GENERAL.nameLlamas.get()) {
				return false;
			}
		}
		else {
			return false;
		}
		return true;
	}
}
