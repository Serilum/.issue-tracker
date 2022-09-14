/*
 * This is the latest source code of Altered Damage.
 * Minecraft version: 1.19.2, mod version: 2.0.
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
