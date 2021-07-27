/*
 * This is the latest source code of Mooshroom Tweaks.
 * Minecraft version: 1.17.1, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Mooshroom Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.mooshroomtweaks.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.mooshroomtweaks.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MooshroomEvent {
	public static Boolean checkNext = false;
	
	@SubscribeEvent
	public void onClick(RightClickBlock e) {
		if (!ConfigHandler.GENERAL.onEggSpawn.get()) {
			return;
		}
		
		ItemStack mainhand = e.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
		if (mainhand.getItem() instanceof SpawnEggItem == false) {
			return;
		}
		checkNext = true;
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		if (!ConfigHandler.GENERAL.onEggSpawn.get()) {
			return;
		}		
		
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide()) {
			return;
		}
		if (entity instanceof MushroomCow == false) {
			return;
		}
		if (!checkNext) {
			return;
		}
		checkNext = false;
		
		double num = Math.random();
		if (num >= ConfigHandler.GENERAL.becomeBrownChance.get()) {
			return;
		}	
		
		MushroomCow mooshroom = (MushroomCow)entity;
		processMooshroom(mooshroom);
	}
	
	@SubscribeEvent
	public void mooshroomSpawn(LivingSpawnEvent.CheckSpawn e) {
		if (!ConfigHandler.GENERAL.onWorldSpawn.get()) {
			return;
		}
		
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide()) {
			return;
		}
		if (entity instanceof MushroomCow == false) {
			return;
		}
		double num = Math.random();
		if (num >= ConfigHandler.GENERAL.becomeBrownChance.get()) {
			return;
		}
		
		MushroomCow mooshroom = (MushroomCow)entity;
		processMooshroom(mooshroom);
	}
	
	public void processMooshroom(MushroomCow mooshroom) {
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
