/*
 * This is the latest source code of Altered Damage.
 * Minecraft version: 1.19.2, mod version: 1.8.
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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void onEntityDamageTaken(LivingHurtEvent e) {
		Entity target = e.getEntity();
		Level world = target.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
			
		Double modifier = 1.0;
		
		if (target instanceof Player) {
			if (!ConfigHandler.GENERAL.alterPlayerDamageTaken.get()) {
				return;
			}
			
			modifier = ConfigHandler.GENERAL.playerDamageModifier.get();
		}
		else {
			if (!ConfigHandler.GENERAL.alterEntityDamageTaken.get()) {
				return;
			}
			
			modifier = ConfigHandler.GENERAL.entityDamageModifier.get();
		}
		
		float damage = (float)(e.getAmount()*modifier);
		
		if (ConfigHandler.GENERAL.preventFatalModifiedDamage.get()) {
			LivingEntity le = (LivingEntity)target;
			float health = (float)Math.floor(le.getHealth());
			if (damage >= health) {
				return;
			}
		}
		
		e.setAmount(damage);
	}
}
