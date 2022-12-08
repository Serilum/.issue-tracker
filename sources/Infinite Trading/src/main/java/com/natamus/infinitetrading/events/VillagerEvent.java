/*
 * This is the latest source code of Infinite Trading.
 * Minecraft version: 1.19.3, mod version: 3.0.
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

package com.natamus.infinitetrading.events;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.infinitetrading.config.ConfigHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillagerEvent {
	@SubscribeEvent
	public void onVillagerClick(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity target = e.getTarget();
		if (target instanceof Villager) {
			if (ConfigHandler.GENERAL.villagerInfiniteTrades.get()) {
				Villager villager = (Villager) target;
				EntityFunctions.resetMerchantOffers(villager);
			}
		}
		else if (target instanceof WanderingTrader) {
			if (ConfigHandler.GENERAL.wanderingTraderInfiniteTrades.get()) {
				WanderingTrader wanderer = (WanderingTrader)target;
				EntityFunctions.resetMerchantOffers(wanderer);
			}
		}
	}
}
