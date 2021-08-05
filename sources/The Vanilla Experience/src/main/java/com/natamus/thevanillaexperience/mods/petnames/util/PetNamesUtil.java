/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
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

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.MuleEntity;

public class PetNamesUtil {
	public static boolean isNamable(Entity entity) {
		if (entity instanceof WolfEntity) {
			if (!PetNamesConfigHandler.GENERAL.nameWolves.get()) {
				return false;
			}
		}
		else if (entity instanceof CatEntity) {
			if (!PetNamesConfigHandler.GENERAL.nameCats.get()) {
				return false;
			}
		}
		else if (entity instanceof HorseEntity) {
			if (!PetNamesConfigHandler.GENERAL.nameHorses.get()) {
				return false;
			}
		}
		else if (entity instanceof DonkeyEntity) {
			if (!PetNamesConfigHandler.GENERAL.nameDonkeys.get()) {
				return false;
			}
		}
		else if (entity instanceof MuleEntity) {
			if (!PetNamesConfigHandler.GENERAL.nameMules.get()) {
				return false;
			}
		}
		else if (entity instanceof LlamaEntity) {
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
