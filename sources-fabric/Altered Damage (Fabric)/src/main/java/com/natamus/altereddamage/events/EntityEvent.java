/*
 * This is the latest source code of Altered Damage.
 * Minecraft version: 1.19.x, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Altered Damage ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.altereddamage.events;

import com.natamus.altereddamage.config.ConfigHandler;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityEvent {
	public static float onEntityDamageTaken(Level world, Entity target, DamageSource damageSource, float damageAmount) {
		if (world.isClientSide) {
			return damageAmount;
		}
			
		Double modifier = 1.0;
		
		if (target instanceof Player) {
			if (!ConfigHandler.alterPlayerDamageTaken.getValue()) {
				return damageAmount;
			}
			
			modifier = ConfigHandler.playerDamageModifier.getValue();
		}
		else {
			if (!ConfigHandler.alterEntityDamageTaken.getValue()) {
				return damageAmount;
			}
			
			modifier = ConfigHandler.entityDamageModifier.getValue();
		}
		
		float damage = (float)(damageAmount*modifier);
		
		if (ConfigHandler.preventFatalModifiedDamage.getValue()) {
			LivingEntity le = (LivingEntity)target;
			float health = (float)Math.floor(le.getHealth());
			if (damage >= health) {
				return damageAmount;
			}
		}
		
		return damage;
	}
}
