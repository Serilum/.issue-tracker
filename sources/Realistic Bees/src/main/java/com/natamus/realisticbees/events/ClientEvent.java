/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.2, mod version: 2.8.
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

import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.realisticbees.config.ConfigHandler;
import com.natamus.realisticbees.renderer.CustomBeeRenderer;
import com.natamus.realisticbees.util.Reference;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Reference.MOD_ID, bus=Bus.MOD, value=Dist.CLIENT)
public class ClientEvent {
	@SubscribeEvent
    public static void loadComplete(EntityRenderersEvent.RegisterRenderers e) {
		if (ConfigFunctions.getDictValues(Reference.MOD_ID).size() > 0 && ConfigFunctions.getDictValues(Reference.MOD_ID).get("beeSizeModifier") != null) {
			if (ConfigFunctions.getDictValues(Reference.MOD_ID).get("beeSizeModifier").equals("1.0")) {
				return;
			}
		}
		
		e.registerEntityRenderer(EntityType.BEE, manager -> new CustomBeeRenderer(manager));
	}
}
