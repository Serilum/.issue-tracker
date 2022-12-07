/*
 * This is the latest source code of Edibles.
 * Minecraft version: 1.19.3, mod version: 3.1.
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

package com.natamus.edibles.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.natamus.collective_fabric.functions.WorldFunctions;
import com.natamus.edibles.config.ConfigHandler;
import com.natamus.edibles.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

public class EdibleEvent {
	private static final List<Item> edibles = new ArrayList<Item>(Arrays.asList(Items.BLAZE_POWDER, Items.MAGMA_CREAM, Items.SUGAR, Items.GHAST_TEAR, Items.PHANTOM_MEMBRANE, Items.RABBIT_FOOT, Items.GLOWSTONE_DUST));
	private static Map<String, Map<Item, Integer>> playeruses = new HashMap<String, Map<Item,Integer>>();
	private static int currentday = -1;
	
	public static InteractionResult onBlockClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		if (hand.equals(InteractionHand.OFF_HAND)) {
			Item mainhanditem = player.getMainHandItem().getItem();
			if (edibles.contains(mainhanditem)) {
				player.getInventory().setChanged();
				return InteractionResult.FAIL;
			}
		}
		
		return InteractionResult.PASS;
	}
	
	public static InteractionResultHolder<ItemStack> onClickEmpty(Player player, Level world, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (world.isClientSide) {
			return InteractionResultHolder.pass(itemstack);
		}
		
		String playername = player.getName().getString();
		
		Item item = itemstack.getItem();
		if (item.equals(Items.BLAZE_POWDER)) {
			if (ConfigHandler.blazePowderStrengthDurationSeconds == 0) {
				return InteractionResultHolder.pass(itemstack);
			}
			if (!Util.canPlayerUse(playername)) {
				return InteractionResultHolder.pass(itemstack);
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.DAMAGE_BOOST, ConfigHandler.blazePowderStrengthDurationSeconds*20);
			player.addEffect(effect);
		}
		else if (item.equals(Items.MAGMA_CREAM)) {
			if (ConfigHandler.magmaCreamFireResistanceDurationSeconds == 0) {
				return InteractionResultHolder.pass(itemstack);
			}
			if (!Util.canPlayerUse(playername)) {
				return InteractionResultHolder.pass(itemstack);
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.FIRE_RESISTANCE, ConfigHandler.magmaCreamFireResistanceDurationSeconds*20);
			player.addEffect(effect);
		}
		else if (item.equals(Items.SUGAR)) {
			if (ConfigHandler.sugarSpeedDurationSeconds == 0) {
				return InteractionResultHolder.pass(itemstack);
			}
			if (!Util.canPlayerUse(playername)) {
				return InteractionResultHolder.pass(itemstack);
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, ConfigHandler.sugarSpeedDurationSeconds*20);
			player.addEffect(effect);
		}
		else if (item.equals(Items.GHAST_TEAR)) {
			if (ConfigHandler.ghastTearDurationSeconds == 0) {
				return InteractionResultHolder.pass(itemstack);
			}
			if (!Util.canPlayerUse(playername)) {
				return InteractionResultHolder.pass(itemstack);
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.REGENERATION, ConfigHandler.ghastTearDurationSeconds*20);
			player.addEffect(effect);
		}
		else if (item.equals(Items.PHANTOM_MEMBRANE)) {
			if (ConfigHandler.phantomMembraneDurationSeconds == 0) {
				return InteractionResultHolder.pass(itemstack);
			}
			if (!Util.canPlayerUse(playername)) {
				return InteractionResultHolder.pass(itemstack);
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.SLOW_FALLING, ConfigHandler.phantomMembraneDurationSeconds*20);
			player.addEffect(effect);
		}
		else if (item.equals(Items.RABBIT_FOOT)) {
			if (ConfigHandler.rabbitsFootDurationSeconds == 0) {
				return InteractionResultHolder.pass(itemstack);
			}
			if (!Util.canPlayerUse(playername)) {
				return InteractionResultHolder.pass(itemstack);
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.JUMP, ConfigHandler.rabbitsFootDurationSeconds*20);
			player.addEffect(effect);
		}
		else if (item.equals(Items.GLOWSTONE_DUST)) {
			if (ConfigHandler.glowEntityDurationSeconds == 0) {
				return InteractionResultHolder.pass(itemstack);
			}
			if (!Util.canPlayerUse(playername)) {
				return InteractionResultHolder.pass(itemstack);
			}
			
			BlockPos pos = player.blockPosition();
			int r = ConfigHandler.glowEntitiesAroundAffectedRadiusBlocks;
			MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, ConfigHandler.glowEntityDurationSeconds*200);
			
			Iterator<Entity> it = world.getEntities(player, new AABB(pos.getX()-r, pos.getY()-r, pos.getZ()-r, pos.getX()+r, pos.getY()+r, pos.getZ()+r)).iterator();
			while (it.hasNext()) {
				Entity ne = it.next();
				if (ne instanceof LivingEntity && (ne instanceof Player == false)) {
					((LivingEntity)ne).addEffect(effect);
				}
			}		
		}
		else {
			return InteractionResultHolder.pass(itemstack);
		}
		
		player.swing(hand);
		
		Util.setPlayerUse(playername);
		if (!player.isCreative()) {
			itemstack.shrink(1);
		}

		if (ConfigHandler.maxItemUsesPerDaySingleItem != -1 || ConfigHandler.maxItemUsesPerDayTotal != -1) {
			int days = WorldFunctions.getTotalDaysPassed((ServerLevel)world);
			if (currentday != days) {
				playeruses = new HashMap<String, Map<Item, Integer>>();
				currentday = days;
			}
			
			Map<Item, Integer> currentuses = new HashMap<Item, Integer>();
			if (playeruses.containsKey(playername)) {
				currentuses = playeruses.get(playername);
			}
			
			int dayuses = 1;
			if (currentuses.containsKey(item)) {
				dayuses = currentuses.get(item) + 1;
			}
			currentuses.put(item, dayuses);
			
			if (dayuses > ConfigHandler.maxItemUsesPerDaySingleItem && ConfigHandler.maxItemUsesPerDaySingleItem != -1) {
				MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, ConfigHandler.weaknessDurationSeconds*20);
				player.addEffect(weakness);
			}
			else if (currentuses.size() > 1 && ConfigHandler.maxItemUsesPerDayTotal != -1) {
				int totaluses = 0;
				for (int dayuse : currentuses.values()) {
					totaluses += dayuse;
				}
				
				if (totaluses > ConfigHandler.maxItemUsesPerDayTotal) {
					MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, ConfigHandler.weaknessDurationSeconds*20);
					player.addEffect(weakness);	
				}
			}
			
			playeruses.put(playername, currentuses);
		}
		
		return InteractionResultHolder.success(itemstack);
	}
}