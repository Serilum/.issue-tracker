/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.edibles.events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.edibles.config.EdiblesConfigHandler;
import com.natamus.thevanillaexperience.mods.edibles.util.EdiblesUtil;

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
public class EdiblesEdibleEvent {
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
			if (EdiblesConfigHandler.OTHER.blazePowderStrengthDurationSeconds.get() == 0) {
				return;
			}
			if (!EdiblesUtil.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.DAMAGE_BOOST, EdiblesConfigHandler.OTHER.blazePowderStrengthDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.MAGMA_CREAM)) {
			if (EdiblesConfigHandler.OTHER.magmaCreamFireResistanceDurationSeconds.get() == 0) {
				return;
			}
			if (!EdiblesUtil.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.FIRE_RESISTANCE, EdiblesConfigHandler.OTHER.magmaCreamFireResistanceDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.SUGAR)) {
			if (EdiblesConfigHandler.OTHER.sugarSpeedDurationSeconds.get() == 0) {
				return;
			}
			if (!EdiblesUtil.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EdiblesConfigHandler.OTHER.sugarSpeedDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.GHAST_TEAR)) {
			if (EdiblesConfigHandler.OTHER.ghastTearDurationSeconds.get() == 0) {
				return;
			}
			if (!EdiblesUtil.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.REGENERATION, EdiblesConfigHandler.OTHER.ghastTearDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.PHANTOM_MEMBRANE)) {
			if (EdiblesConfigHandler.OTHER.phantomMembraneDurationSeconds.get() == 0) {
				return;
			}
			if (!EdiblesUtil.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.SLOW_FALLING, EdiblesConfigHandler.OTHER.phantomMembraneDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.RABBIT_FOOT)) {
			if (EdiblesConfigHandler.OTHER.rabbitsFootDurationSeconds.get() == 0) {
				return;
			}
			if (!EdiblesUtil.canPlayerUse(playername)) {
				return;
			}
			
			MobEffectInstance effect = new MobEffectInstance(MobEffects.JUMP, EdiblesConfigHandler.OTHER.rabbitsFootDurationSeconds.get()*20);
			player.addEffect(effect);
		}
		else if (itemstack.getItem().equals(Items.GLOWSTONE_DUST)) {
			if (EdiblesConfigHandler.GLOW.glowEntityDurationSeconds.get() == 0) {
				return;
			}
			if (!EdiblesUtil.canPlayerUse(playername)) {
				return;
			}
			
			BlockPos pos = player.blockPosition();
			int r = EdiblesConfigHandler.GLOW.glowEntitiesAroundAffectedRadiusBlocks.get();
			MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, EdiblesConfigHandler.GLOW.glowEntityDurationSeconds.get()*200);
			
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
		
		EdiblesUtil.setPlayerUse(playername);
		if (!player.isCreative()) {
			itemstack.shrink(1);
		}

		if (EdiblesConfigHandler.WEAKNESS.maxItemUsesPerDaySingleItem.get() != -1 || EdiblesConfigHandler.WEAKNESS.maxItemUsesPerDayTotal.get() != -1) {
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
			
			if (dayuses > EdiblesConfigHandler.WEAKNESS.maxItemUsesPerDaySingleItem.get() && EdiblesConfigHandler.WEAKNESS.maxItemUsesPerDaySingleItem.get() != -1) {
				MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, EdiblesConfigHandler.WEAKNESS.weaknessDurationSeconds.get()*20);
				player.addEffect(weakness);
			}
			else if (currentuses.size() > 1 && EdiblesConfigHandler.WEAKNESS.maxItemUsesPerDayTotal.get() != -1) {
				int totaluses = 0;
				for (int dayuse : currentuses.values()) {
					totaluses += dayuse;
				}
				
				if (totaluses > EdiblesConfigHandler.WEAKNESS.maxItemUsesPerDayTotal.get()) {
					MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, EdiblesConfigHandler.WEAKNESS.weaknessDurationSeconds.get()*20);
					player.addEffect(weakness);	
				}
			}
			
			playeruses.put(playername, currentuses);
		}
	}
}