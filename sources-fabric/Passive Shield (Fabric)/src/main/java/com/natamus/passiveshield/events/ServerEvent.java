/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.19.2, mod version: 2.6.
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
