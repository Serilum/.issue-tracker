/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.17.1, mod version: 2.0.
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
import com.natamus.realisticbees.renderer.CustomBeeRenderer;
import com.natamus.realisticbees.util.Reference;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Reference.MOD_ID, bus=Bus.MOD, value=Dist.CLIENT)
public class ClientEvent {
	@SubscribeEvent
    public static void loadComplete(EntityRenderersEvent.RegisterRenderers e) {
		if (ConfigHandler.GENERAL.beeSizeModifier.get() == 1.0) {
			return;
		}
		
		e.registerEntityRenderer(EntityType.BEE, new EntityRendererProvider<Bee>() {
			@Override
			public EntityRenderer<Bee> create(Context manager) {
				return new CustomBeeRenderer(manager);
			}
		});
	}
}
