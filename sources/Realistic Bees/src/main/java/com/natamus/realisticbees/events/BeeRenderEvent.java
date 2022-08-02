/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.1, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.realisticbees.events;

import com.natamus.realisticbees.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class BeeRenderEvent {
	@SubscribeEvent
	public static void onEntitySize(EntityEvent.Size e) {
		if (ConfigHandler.GENERAL.beeSizeModifier.get() == 1.0) {
			return;
		}
		
		Entity entity = e.getEntity();
		
		if (!(entity instanceof Bee)) {
			return;
		}
		
		EntityDimensions size = e.getNewSize().scale(ConfigHandler.GENERAL.beeSizeModifier.get().floatValue());
		e.setNewSize(size, true);
	}
}
