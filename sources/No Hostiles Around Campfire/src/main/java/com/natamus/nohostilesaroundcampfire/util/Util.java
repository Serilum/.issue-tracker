/*
 * This is the latest source code of No Hostiles Around Campfire.
 * Minecraft version: 1.19.2, mod version: 4.5.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.nohostilesaroundcampfire.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective.functions.EntityFunctions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.ForgeRegistries;

public class Util {
	private static List<String> hostileSpecialEntities = new ArrayList<String>(Arrays.asList("FoxHound"));
	private static List<String> hostileSpecialResourceLocations = new ArrayList<String>(Arrays.asList("lycanitesmobs"));
	
	public static boolean entityIsHostile(Entity entity) {
		if (entity.getType().getCategory().equals(MobCategory.MONSTER)) {
			return true;
		}
		
		String entitystring = EntityFunctions.getEntityString(entity);
		if (hostileSpecialEntities.contains(entitystring)) {
			return true;
		}
		
		ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
		if (rl != null) {
			if (hostileSpecialResourceLocations.contains(rl.toString().split(":")[0])) {
				return true;
			}
		}
		
		return false;
	}
}
