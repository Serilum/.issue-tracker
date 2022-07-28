/*
 * This is the latest source code of Inventory Totem.
 * Minecraft version: 1.19.1, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Inventory Totem ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.inventorytotem.events;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class TotemEvent {
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof Player == false) {
			return;
		}
		
		Player player = (Player)entity;
		if (player.getMainHandItem().getItem().equals(Items.TOTEM_OF_UNDYING) || player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)) {
			return;
		}
		
		Inventory inv = player.getInventory();
		if (inv == null) {
			return;
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
			return;
		}
		
		e.setCanceled(true);
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
	}
}
