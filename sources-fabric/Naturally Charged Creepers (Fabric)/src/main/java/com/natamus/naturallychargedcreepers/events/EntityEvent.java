/*
 * This is the latest source code of Naturally Charged Creepers.
 * Minecraft version: 1.19.2, mod version: 2.3.
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

package com.natamus.naturallychargedcreepers.events;

import java.util.Set;

import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.naturallychargedcreepers.config.ConfigHandler;
import com.natamus.naturallychargedcreepers.util.Reference;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

public class EntityEvent {
	public static void onEntityJoin(Level world, Entity entity) {
		if (world.isClientSide()) {
			return;
		}
		
		if (entity instanceof Creeper == false) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".checked")) {
			return;
		}
		entity.addTag(Reference.MOD_ID + ".checked");
		
		double num = Math.random();
		if (num < ConfigHandler.isChargedChance.getValue()) {
			EntityFunctions.chargeEntity(entity);
		}	
	}
}
