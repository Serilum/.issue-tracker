/*
 * This is the latest source code of Superflat World No Slimes.
 * Minecraft version: 1.19.2, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.superflatworldnoslimes.events;

import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SlimeEvent {
	@SubscribeEvent
	public void onWorldJoin(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
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
