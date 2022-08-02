/*
 * This is the latest source code of Pet Names.
 * Minecraft version: 1.19.1, mod version: 2.1.
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
