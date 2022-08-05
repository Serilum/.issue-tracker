/*
 * This is the latest source code of Inventory Totem.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.inventorytotem.events;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TotemEvent {
	public static boolean allowPlayerDeath (ServerLevel world, ServerPlayer player) {
		if (player.getMainHandItem().getItem().equals(Items.TOTEM_OF_UNDYING) || player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)) {
			return true;
		}
		
		Inventory inv = player.getInventory();
		if (inv == null) {
			return true;
		}
		
		ItemStack totemstack = null;
		for(int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.getItem().equals(Items.TOTEM_OF_UNDYING)) {
				totemstack = stack;
				break;
			}
		}
		
		if (totemstack == null) {
			return true;
		}
		
		if (player instanceof ServerPlayer) {
			ServerPlayer entityplayermp = (ServerPlayer)player;
            entityplayermp.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
            CriteriaTriggers.USED_TOTEM.trigger(entityplayermp, totemstack);
        }

        player.setHealth(1.0F);
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        world.broadcastEntityEvent(player, (byte)35);
        totemstack.shrink(1);
        return false;
	}
}
