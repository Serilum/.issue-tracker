/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.1, mod version: 2.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Realistic Bees ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
