/*
 * This is the latest source code of Naturally Charged Creepers.
 * Minecraft version: 1.17.x, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Naturally Charged Creepers ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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