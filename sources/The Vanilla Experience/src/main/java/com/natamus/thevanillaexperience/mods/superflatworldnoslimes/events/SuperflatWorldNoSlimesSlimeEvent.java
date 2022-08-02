/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.superflatworldnoslimes.events;

import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SuperflatWorldNoSlimesSlimeEvent {
	@SubscribeEvent
	public void onWorldJoin(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		
		if (serverworld.getServer().getWorldData().worldGenSettings().isFlatWorld()) {
			if (e.getEntity() instanceof Slime) {
				e.setCanceled(true);
			}
		}
	}
}
