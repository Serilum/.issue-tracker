/*
 * This is the latest source code of Infinite Trading.
 * Minecraft version: 1.19.2, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.infinitetrading.events;

import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.infinitetrading.config.ConfigHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class VillagerEvent {
	public static InteractionResult onVillagerClick(Player player, Level world, InteractionHand hand, Entity target, EntityHitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}

		if (target instanceof Villager) {
			if (ConfigHandler.villagerInfiniteTrades.getValue()) {
				Villager villager = (Villager) target;
				EntityFunctions.resetMerchantOffers(villager);
			}
		}
		else if (target instanceof WanderingTrader) {
			if (ConfigHandler.wanderingTraderInfiniteTrades.getValue()) {
				WanderingTrader wanderer = (WanderingTrader)target;
				EntityFunctions.resetMerchantOffers(wanderer);
			}
		}

		return InteractionResult.PASS;
	}
}
