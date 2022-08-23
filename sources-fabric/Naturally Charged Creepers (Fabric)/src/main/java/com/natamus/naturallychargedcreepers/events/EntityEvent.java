/*
 * This is the latest source code of Naturally Charged Creepers.
 * Minecraft version: 1.19.2, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
