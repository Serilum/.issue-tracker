/*
 * This is the latest source code of No Hostiles Around Campfire.
 * Minecraft version: 1.17.1, mod version: 3.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of No Hostiles Around Campfire ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.nohostilesaroundcampfire.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective.functions.EntityFunctions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;

public class Util {
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
