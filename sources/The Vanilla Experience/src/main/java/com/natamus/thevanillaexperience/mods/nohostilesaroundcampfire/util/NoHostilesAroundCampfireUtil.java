/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.nohostilesaroundcampfire.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective.functions.EntityFunctions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;

public class NoHostilesAroundCampfireUtil {
	private static List<String> hostileSpecialEntities = new ArrayList<String>(Arrays.asList("FoxHound"));
	
	public static boolean entityIsHostile(Entity entity) {
		if (entity.getType().getCategory().equals(MobCategory.MONSTER)) {
			return true;
		}
		
		String entitystring = EntityFunctions.getEntityString(entity);
		if (hostileSpecialEntities.contains(entitystring)) {
			return true;
		}
		
		return false;
	}
}
