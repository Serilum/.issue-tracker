/*
 * This is the latest source code of Conduits Prevent Drowned.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Conduits Prevent Drowned ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.conduitspreventdrowned.events;

import java.util.Collection;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.conduitspreventdrowned.config.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DrownedEvent {
	@SubscribeEvent
	public void onDrownedSpawn(LivingSpawnEvent.CheckSpawn e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof DrownedEntity == false) {
			return;
		}
		
		BlockPos epos = entity.blockPosition();
		int r = ConfigHandler.GENERAL.preventDrownedInRange.get();
		
		for (PlayerEntity player : world.players()) {
			BlockPos playerpos = new BlockPos(player.getX(), 1, player.getZ());
			if (playerpos.closerThan(new BlockPos(epos.getX(), 1, epos.getZ()), r)) {
				Collection<EffectInstance> activeeffects = player.getActiveEffects();
				if (activeeffects.size() > 0) {
					boolean foundconduit = false;
					for (EffectInstance pe : activeeffects) {
						if (pe.getEffect().equals(Effects.CONDUIT_POWER)) {
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
