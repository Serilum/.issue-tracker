/*
 * This is the latest source code of Breedable Killer Rabbit.
 * Minecraft version: 1.16.5, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Breedable Killer Rabbit ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.breedablekillerrabbit.events;

import com.natamus.breedablekillerrabbit.config.ConfigHandler;
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.StringFunctions;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void onBaby(BabyEntitySpawnEvent e) {
		AgeableEntity child = e.getChild();
		World world = child.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (child instanceof RabbitEntity == false) {
			return;
		}
		RabbitEntity rabbit = (RabbitEntity)child;
		
		double num = GlobalVariables.random.nextDouble();
		if (num <= ConfigHandler.GENERAL.chanceBabyRabbitIsKiller.get()) {
			rabbit.setRabbitType(99);
			if (ConfigHandler.GENERAL.removeKillerRabbitNameTag.get()) {
				rabbit.setCustomName(null);
			}
			
			PlayerEntity player = e.getCausedByPlayer();
			if (player == null) {
				return;
			}
			StringFunctions.sendMessage(player, "A killer rabbit has been born! Are you far enough away or do you have a golden carrot to share?", TextFormatting.DARK_GREEN);
		}
	}
	
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		Entity entity = e.getTarget();
		if (entity instanceof RabbitEntity == false) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
		ItemStack itemstack = e.getItemStack();
		
		if (!itemstack.getItem().equals(Items.GOLDEN_CARROT)) {
			return;
		}
		
		RabbitEntity rabbit = (RabbitEntity)entity;
		if (rabbit.getRabbitType() != 99) {
			return;
		}
		if (rabbit.getHeldItem(Hand.MAIN_HAND).getItem().equals(Items.GOLDEN_CARROT)) {
			StringFunctions.sendMessage(player, "The killer rabbit has already been tamed.", TextFormatting.DARK_GREEN);
			return;
		}
		
		rabbit.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.GOLDEN_CARROT, 1));
		itemstack.shrink(1);
		StringFunctions.sendMessage(player, "The killer rabbit has been tamed!", TextFormatting.DARK_GREEN);
	}
	
	@SubscribeEvent
	public void onTarget(LivingAttackEvent e) {
		Entity source = e.getSource().getImmediateSource();
		if (source == null) {
			return;
		}
		
		World world = source.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (source instanceof RabbitEntity == false) {
			return;
		}
		
		if (((RabbitEntity)source).getHeldItem(Hand.MAIN_HAND).getItem().equals(Items.GOLDEN_CARROT)) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void mobSpawn(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof RabbitEntity == false) {
			return;
		}
		if (!ConfigHandler.GENERAL.removeKillerRabbitNameTag.get()) {
			return;
		}
		if (((RabbitEntity)entity).getRabbitType() != 99) {
			return;
		}
		if (!entity.hasCustomName()) {
			return;
		}
		
		if (entity.getCustomName().getString().equals("The Killer Bunny")) {
			entity.setCustomName(null);
		}
	}
	
	@SubscribeEvent
	public void onPlayerDamage(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		Entity source = e.getSource().getImmediateSource();
		if (source == null) {
			return;
		}
		if (source instanceof RabbitEntity == false) {
			return;
		}
		
		if (((RabbitEntity)source).getRabbitType() == 99) {
			StringFunctions.sendMessage((PlayerEntity)e.getEntity(), "The killer rabbit wants a golden carrot!", TextFormatting.RED);
		}
	}
}
