/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.passiveshield.events;

import com.natamus.passiveshield.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ServerEvent {
	@SubscribeEvent
	public void onEntityDamageTaken(LivingHurtEvent e) {
		Entity target = e.getEntity();
		Level world = target.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
			
		if (target instanceof Player) {
			Player player = (Player)target;
			if (!(player.getMainHandItem().getItem() instanceof ShieldItem)) {
				if (!(player.getOffhandItem().getItem() instanceof ShieldItem)) {
					return;
				}
			}
			
			double modifier = 1.0 -ConfigHandler.GENERAL.passiveShieldPercentageDamageNegated.get();
			e.setAmount((float)(e.getAmount()*modifier));
		}
	}
}
