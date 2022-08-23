/*
 * This is the latest source code of Naturally Charged Creepers.
 * Minecraft version: 1.19.2, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.naturallychargedcreepers.events;

import java.util.Set;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.naturallychargedcreepers.config.ConfigHandler;
import com.natamus.naturallychargedcreepers.util.Reference;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void onEntityJoin(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide()) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Creeper == false) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".checked")) {
			return;
		}
		entity.addTag(Reference.MOD_ID + ".checked");
		
		double num = Math.random();
		if (num < ConfigHandler.GENERAL.isChargedChance.get()) {
			EntityFunctions.chargeEntity(entity);
		}	
	}
}
