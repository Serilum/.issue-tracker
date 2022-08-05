/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.19.2, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.passiveshield.events;

import com.natamus.passiveshield.config.ConfigHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;

public class ServerEvent {
	public static float onEntityDamageTaken(Level world, Entity entity, DamageSource damageSource, float damageAmount) {
		if (world.isClientSide) {
			return damageAmount;
		}

		Entity target = damageSource.getDirectEntity();
		if (target instanceof Player) {
			Player player = (Player)target;
			if (!(player.getMainHandItem().getItem() instanceof ShieldItem)) {
				if (!(player.getOffhandItem().getItem() instanceof ShieldItem)) {
					return damageAmount;
				}
			}
			
			double modifier = 1.0 -ConfigHandler.passiveShieldPercentageDamageNegated.getValue();
			return (float)(damageAmount*modifier);
		}

		return damageAmount;
	}
}
