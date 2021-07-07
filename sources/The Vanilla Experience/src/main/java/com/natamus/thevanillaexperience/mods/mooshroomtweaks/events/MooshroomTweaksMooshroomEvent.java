/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.mooshroomtweaks.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.thevanillaexperience.mods.mooshroomtweaks.config.MooshroomTweaksConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MooshroomTweaksMooshroomEvent {
	public static Boolean checkNext = false;
	
	@SubscribeEvent
	public void onClick(RightClickBlock e) {
		if (!MooshroomTweaksConfigHandler.GENERAL.onEggSpawn.get()) {
			return;
		}
		
		ItemStack mainhand = e.getPlayer().getItemInHand(Hand.MAIN_HAND);
		if (mainhand.getItem() instanceof SpawnEggItem == false) {
			return;
		}
		checkNext = true;
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		if (!MooshroomTweaksConfigHandler.GENERAL.onEggSpawn.get()) {
			return;
		}		
		
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide()) {
			return;
		}
		if (entity instanceof MooshroomEntity == false) {
			return;
		}
		if (!checkNext) {
			return;
		}
		checkNext = false;
		
		double num = Math.random();
		if (num >= MooshroomTweaksConfigHandler.GENERAL.becomeBrownChance.get()) {
			return;
		}	
		
		MooshroomEntity mooshroom = (MooshroomEntity)entity;
		processMooshroom(mooshroom);
	}
	
	@SubscribeEvent
	public void mooshroomSpawn(LivingSpawnEvent.CheckSpawn e) {
		if (!MooshroomTweaksConfigHandler.GENERAL.onWorldSpawn.get()) {
			return;
		}
		
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide()) {
			return;
		}
		if (entity instanceof MooshroomEntity == false) {
			return;
		}
		double num = Math.random();
		if (num >= MooshroomTweaksConfigHandler.GENERAL.becomeBrownChance.get()) {
			return;
		}
		
		MooshroomEntity mooshroom = (MooshroomEntity)entity;
		processMooshroom(mooshroom);
	}
	
	public void processMooshroom(MooshroomEntity mooshroom) {
		Timer timer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EntityFunctions.chargeEntity(mooshroom);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
}
