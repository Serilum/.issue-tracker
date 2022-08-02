/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.naturallychargedcreepers.events;

import java.util.Set;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.thevanillaexperience.mods.naturallychargedcreepers.config.NaturallyChargedCreepersConfigHandler;
import com.natamus.thevanillaexperience.mods.naturallychargedcreepers.util.Reference;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class NaturallyChargedCreepersEntityEvent {
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
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
		if (num < NaturallyChargedCreepersConfigHandler.GENERAL.isChargedChance.get()) {
			EntityFunctions.chargeEntity(entity);
		}	
	}
}
