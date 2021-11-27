/*
 * This is the latest source code of Conduits Prevent Drowned.
 * Minecraft version: 1.17.x, mod version: 1.5.
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

import com.natamus.conduitspreventdrowned.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;

public class DrownedEvent {
	public static boolean onDrownedSpawn(Mob entity, ServerLevel world, BlockPos spawnerPos, MobSpawnType spawnReason) {
		if (entity instanceof Drowned == false) {
			return true;
		}
		
		BlockPos epos = entity.blockPosition();
		int r = ConfigHandler.preventDrownedInRange.getValue();
		
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
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
