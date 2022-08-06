/*
 * This is the latest source code of Conduits Prevent Drowned.
 * Minecraft version: 1.19.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.conduitspreventdrowned.events;

import java.util.Collection;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.conduitspreventdrowned.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DrownedEvent {
	@SubscribeEvent
	public void onDrownedSpawn(LivingSpawnEvent.CheckSpawn e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Drowned == false) {
			return;
		}
		
		BlockPos epos = entity.blockPosition();
		int r = ConfigHandler.GENERAL.preventDrownedInRange.get();
		
		for (Player player : world.players()) {
			BlockPos playerpos = new BlockPos(player.getX(), 1, player.getZ());
			if (playerpos.closerThan(new BlockPos(epos.getX(), 1, epos.getZ()), r)) {
				Collection<MobEffectInstance> activeeffects = player.getActiveEffects();
				if (activeeffects.size() > 0) {
					boolean foundconduit = false;
					for (MobEffectInstance pe : activeeffects) {
						if (pe.getEffect().equals(MobEffects.CONDUIT_POWER)) {
							foundconduit = true;
							break;
						}
					}
					if (foundconduit) {
						e.setResult(Result.DENY);
					}
				}
			}
		}
	}
}
