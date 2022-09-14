/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.2, mod version: 2.7.
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
