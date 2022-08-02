/*
 * This is the latest source code of No Hostiles Around Campfire.
 * Minecraft version: 1.19.1, mod version: 4.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.nohostilesaroundcampfire.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective_fabric.functions.EntityFunctions;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;

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
		
		ResourceLocation rl = Registry.ENTITY_TYPE.getKey(entity.getType());
		if (rl != null) {
			if (hostileSpecialResourceLocations.contains(rl.toString().split(":")[0])) {
				return true;
			}
		}
		
		return false;
	}
}
