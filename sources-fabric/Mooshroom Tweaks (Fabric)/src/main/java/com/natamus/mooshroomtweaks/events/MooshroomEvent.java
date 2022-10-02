/*
 * This is the latest source code of Mooshroom Tweaks.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.mooshroomtweaks.events;

import java.util.Set;

import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.mooshroomtweaks.config.ConfigHandler;
import com.natamus.mooshroomtweaks.util.Reference;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.Level;

public class MooshroomEvent {
	public static void onEntityJoin(Level world, Entity entity) {
		if (world.isClientSide()) {
			return;
		}
		
		if (entity instanceof MushroomCow == false) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".checked")) {
			return;
		}
		entity.addTag(Reference.MOD_ID + ".checked");
		
		double num = Math.random();
		if (num < ConfigHandler.becomeBrownChance) {
			EntityFunctions.chargeEntity(entity);
		}	
	}
}
