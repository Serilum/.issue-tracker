/*
 * This is the latest source code of Breedable Killer Rabbit.
 * Minecraft version: 1.19.1, mod version: 1.9.
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

import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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
		AgeableMob child = e.getChild();
		Level world = child.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (child instanceof Rabbit == false) {
			return;
		}
		Rabbit rabbit = (Rabbit)child;
		
		double num = GlobalVariables.random.nextDouble();
		if (num <= ConfigHandler.GENERAL.chanceBabyRabbitIsKiller.get()) {
			rabbit.setRabbitType(99);
			if (ConfigHandler.GENERAL.removeKillerRabbitNameTag.get()) {
				rabbit.setCustomName(null);
			}
			
			Player player = e.getCausedByPlayer();
			if (player == null) {
				return;
			}
			StringFunctions.sendMessage(player, "A killer rabbit has been born! Are you far enough away or do you have a golden carrot to share?", ChatFormatting.DARK_GREEN);
		}
	}
	
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getTarget();
		if (entity instanceof Rabbit == false) {
			return;
		}
		
		Player player = e.getEntity();
		ItemStack itemstack = e.getItemStack();
		
		if (!itemstack.getItem().equals(Items.GOLDEN_CARROT)) {
			return;
		}
		
		Rabbit rabbit = (Rabbit)entity;
		if (rabbit.getRabbitType() != 99) {
			return;
		}
		if (rabbit.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.GOLDEN_CARROT)) {
			StringFunctions.sendMessage(player, "The killer rabbit has already been tamed.", ChatFormatting.DARK_GREEN);
			return;
		}
		
		rabbit.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_CARROT, 1));
		itemstack.shrink(1);
		StringFunctions.sendMessage(player, "The killer rabbit has been tamed!", ChatFormatting.DARK_GREEN);
	}
	
	@SubscribeEvent
	public void onTarget(LivingAttackEvent e) {
		Entity source = e.getSource().getDirectEntity();
		if (source == null) {
			return;
		}
		
		Level world = source.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (source instanceof Rabbit == false) {
			return;
		}
		
		if (((Rabbit)source).getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.GOLDEN_CARROT)) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void mobSpawn(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Rabbit == false) {
			return;
		}
		if (!ConfigHandler.GENERAL.removeKillerRabbitNameTag.get()) {
			return;
		}
		if (((Rabbit)entity).getRabbitType() != 99) {
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
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof Player == false) {
			return;
		}
		
		Entity source = e.getSource().getDirectEntity();
		if (source == null) {
			return;
		}
		if (source instanceof Rabbit == false) {
			return;
		}
		
		if (((Rabbit)source).getRabbitType() == 99) {
			StringFunctions.sendMessage((Player)e.getEntity(), "The killer rabbit wants a golden carrot!", ChatFormatting.RED);
		}
	}
}
