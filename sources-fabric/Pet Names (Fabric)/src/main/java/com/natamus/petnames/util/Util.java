/*
 * This is the latest source code of Pet Names.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
