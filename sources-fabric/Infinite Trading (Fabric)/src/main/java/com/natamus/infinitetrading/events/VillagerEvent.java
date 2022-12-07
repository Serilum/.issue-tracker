/*
 * This is the latest source code of Infinite Trading.
 * Minecraft version: 1.19.3, mod version: 3.2.
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
			if (ConfigHandler.villagerInfiniteTrades) {
				Villager villager = (Villager) target;
				EntityFunctions.resetMerchantOffers(villager);
			}
		}
		else if (target instanceof WanderingTrader) {
			if (ConfigHandler.wanderingTraderInfiniteTrades) {
				WanderingTrader wanderer = (WanderingTrader)target;
				EntityFunctions.resetMerchantOffers(wanderer);
			}
		}

		return InteractionResult.PASS;
	}
}
