/*
 * This is the latest source code of Naturally Charged Creepers.
 * Minecraft version: 1.16.5, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Naturally Charged Creepers ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.naturallychargedcreepers.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.Timer;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.naturallychargedcreepers.config.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
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
public class EntityEvent {
	public static Boolean checkNext = false;
	
	@SubscribeEvent
	public void onClick(RightClickBlock e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!ConfigHandler.GENERAL.onEggSpawn.get()) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
		ItemStack mainhand = player.getItemInHand(Hand.MAIN_HAND);
		if (mainhand.getItem() instanceof SpawnEggItem == false) {
			return;
		}
		checkNext = true;
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!ConfigHandler.GENERAL.onEggSpawn.get()) {
			return;
		}		
		
		if (entity instanceof CreeperEntity == false) {
			return;
		}
		if (!checkNext) {
			return;
		}
		checkNext = false;
		
		double num = Math.random();
		if (num >= ConfigHandler.GENERAL.isChargedChance.get()) {
			return;
		}	
		
		CreeperEntity creeper = (CreeperEntity)entity;
		processCreeper(creeper);
	}
	
	@SubscribeEvent
	public void CreeperSpawn(LivingSpawnEvent.CheckSpawn e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!ConfigHandler.GENERAL.onWorldSpawn.get()) {
			return;
		}
		
		if (entity instanceof CreeperEntity == false) {
			return;
		}
		double num = Math.random();
		if (num >= ConfigHandler.GENERAL.isChargedChance.get()) {
			return;
		}
		
		CreeperEntity creeper = (CreeperEntity)entity;
		processCreeper(creeper);
	}
	
	private static ArrayList<UUID> processeduuids = new ArrayList<UUID>();
	public void processCreeper(CreeperEntity creeper) {
		UUID uuid = creeper.getUUID();
		if (!processeduuids.contains(uuid)) {
			processeduuids.add(uuid);
		}
		else {
			return;
		}
		
		Timer timer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EntityFunctions.chargeEntity(creeper);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
}
