/*
 * This is the latest source code of Edibles.
 * Minecraft version: 1.17.1, mod version: 2.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Edibles ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.edibles.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.edibles.config.ConfigHandler;
import com.natamus.edibles.util.Util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EdibleEvent {
	private static Map<String, Map<Item, Integer>> playeruses = new HashMap<String, Map<Item,Integer>>();
	private static int currentday = -1;
	
	@SubscribeEvent
	public void onClickEmpty(PlayerInteractEvent.RightClickItem e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Player player = e.getPlayer();
		String playername = player.getName().getString();
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.BLAZE_POWDER)) {
			if (ConfigHandler.OTHER.blazePowderStrengthDurationSeconds.get() == 0) {
				return;
			}
			if (!Util.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.DAMAGE_BOOST, ConfigHandler.OTHER.blazePowderStrengthDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.MAGMA_CREAM)) {
			if (ConfigHandler.OTHER.magmaCreamFireResistanceDurationSeconds.get() == 0) {
				return;
			}
			if (!Util.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.FIRE_RESISTANCE, ConfigHandler.OTHER.magmaCreamFireResistanceDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.SUGAR)) {
			if (ConfigHandler.OTHER.sugarSpeedDurationSeconds.get() == 0) {
				return;
			}
			if (!Util.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, ConfigHandler.OTHER.sugarSpeedDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.GHAST_TEAR)) {
			if (ConfigHandler.OTHER.ghastTearDurationSeconds.get() == 0) {
				return;
			}
			if (!Util.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.REGENERATION, ConfigHandler.OTHER.ghastTearDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.PHANTOM_MEMBRANE)) {
			if (ConfigHandler.OTHER.phantomMembraneDurationSeconds.get() == 0) {
				return;
			}
			if (!Util.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.SLOW_FALLING, ConfigHandler.OTHER.phantomMembraneDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.RABBIT_FOOT)) {
			if (ConfigHandler.OTHER.rabbitsFootDurationSeconds.get() == 0) {
				return;
			}
			if (!Util.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.JUMP, ConfigHandler.OTHER.rabbitsFootDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.GLOWSTONE_DUST)) {
			if (ConfigHandler.GLOW.glowEntityDurationSeconds.get() == 0) {
				return;
			}
			if (!Util.canPlayerUse(playername)) {
				return;
			}
			
			BlockPos pos = player.blockPosition();
			int r = ConfigHandler.GLOW.glowEntitiesAroundAffectedRadiusBlocks.get();
			MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, ConfigHandler.GLOW.glowEntityDurationSeconds.get()*200);
			
			Iterator<Entity> it = world.getEntities(player, new AABB(pos.getX()-r, pos.getY()-r, pos.getZ()-r, pos.getX()+r, pos.getY()+r, pos.getZ()+r)).iterator();
			while (it.hasNext()) {
				Entity ne = it.next();
				if (ne instanceof LivingEntity && (ne instanceof Player == false)) {
					((LivingEntity)ne).addEffect(effect);
				}
			}		
		}
		else {
			return;
		}
		
		Util.setPlayerUse(playername);
		if (!player.isCreative()) {
			itemstack.shrink(1);
		}

		if (ConfigHandler.WEAKNESS.maxItemUsesPerDaySingleItem.get() != -1 || ConfigHandler.WEAKNESS.maxItemUsesPerDayTotal.get() != -1) {
			int days = WorldFunctions.getTotalDaysPassed((ServerLevel)world);
			if (currentday != days) {
				playeruses = new HashMap<String, Map<Item, Integer>>();
				currentday = days;
			}
			
			Item itemused = itemstack.getItem();
			
			Map<Item, Integer> currentuses = new HashMap<Item, Integer>();
			if (playeruses.containsKey(playername)) {
				currentuses = playeruses.get(playername);
			}
			
			int dayuses = 1;
			if (currentuses.containsKey(itemused)) {
				dayuses = currentuses.get(itemused) + 1;
			}
			currentuses.put(itemused, dayuses);
			
			if (dayuses > ConfigHandler.WEAKNESS.maxItemUsesPerDaySingleItem.get() && ConfigHandler.WEAKNESS.maxItemUsesPerDaySingleItem.get() != -1) {
				MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, ConfigHandler.WEAKNESS.weaknessDurationSeconds.get()*20);
				player.addEffect(weakness);
			}
			else if (currentuses.size() > 1 && ConfigHandler.WEAKNESS.maxItemUsesPerDayTotal.get() != -1) {
				int totaluses = 0;
				for (int dayuse : currentuses.values()) {
					totaluses += dayuse;
				}
				
				if (totaluses > ConfigHandler.WEAKNESS.maxItemUsesPerDayTotal.get()) {
					MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, ConfigHandler.WEAKNESS.weaknessDurationSeconds.get()*20);
					player.addEffect(weakness);	
				}
			}
			
			playeruses.put(playername, currentuses);
		}
	}
}