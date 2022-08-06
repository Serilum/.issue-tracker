/*
 * This is the latest source code of Pet Names.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.petnames.events;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.petnames.config.ConfigHandler;
import com.natamus.petnames.util.Util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void onBaby(BabyEntitySpawnEvent e) {
		Entity entity = e.getChild();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		if (!Util.isNamable(entity)) {
			return;
		}
		
		EntityFunctions.nameEntity(entity, StringFunctions.getRandomName(ConfigHandler.GENERAL._useMaleNames.get(), ConfigHandler.GENERAL._useFemaleNames.get()));
	}
}
