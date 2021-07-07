/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.16.5, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Respawn Delay ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.respawndelay.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.respawndelay.config.ConfigHandler;
import com.natamus.respawndelay.util.Util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RespawningEvent {
	public static HashMap<PlayerEntity, Date> death_times = new HashMap<PlayerEntity, Date>();
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		PlayerEntity player = e.player;
		World world = player.getCommandSenderWorld();
		if (world.isClientSide || e.phase != Phase.START) {
			return;
		}
		
		if (player.tickCount % 20 != 0) {
			return;
		}
		
		if (!player.isSpectator()) {
			return;
		}
		
		if (!death_times.containsKey(player)) {
			return;
		}
		
		int respawndelay = ConfigHandler.GENERAL.respawnDelayInSeconds.get();
		if (respawndelay < 0) {
			return;
		}
		
		Date now = new Date();
		Date timedied = death_times.get(player); 
		
		long ms = (now.getTime()-timedied.getTime());
		if (ms > respawndelay*1000) {
			Util.respawnPlayer(world, player);
			return;
		}
		
		int seconds = respawndelay - (int)(ms/1000);
		String waitingmessage = ConfigHandler.GENERAL.waitingForRespawnMessage.get();
		waitingmessage = waitingmessage.replaceAll("<seconds_left>", seconds + "");
		
		StringFunctions.sendMessage(player, waitingmessage, TextFormatting.GRAY);
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)entity;
		if (ConfigHandler.GENERAL.ignoreAdministratorPlayers.get()) {
			if (player.hasPermissions(2)) {
				return;
			}
		}
		
		if (ConfigHandler.GENERAL.ignoreCreativePlayers.get()) {
			if (player.isCreative()) {
				return;
			}
		}
		
		e.setCanceled(true);
		player.setGameMode(GameType.SPECTATOR);
		player.setHealth(20);
		player.awardStat(Stats.DEATHS);
		player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
		player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
		player.clearFire();
		
		Vector3d pvec = player.position();
		if (pvec.y() < ConfigHandler.GENERAL.lowestPossibleYCoordinate.get()) {
			pvec = new Vector3d(pvec.x(), ConfigHandler.GENERAL.lowestPossibleYCoordinate.get(), pvec.z());
			player.setDeltaMovement(0, 0, 0);
			player.teleportTo(pvec.x(), pvec.y(), pvec.z());
		}
		
		if (!ConfigHandler.GENERAL.keepItemsOnDeath.get()) {
			Collection<ItemEntity> playerdrops = new ArrayList<ItemEntity>();
			
			PlayerInventory inv = player.inventory;
			for (int i=0; i < 36; i++) {
				ItemStack slot = inv.getItem(i);
				if (!slot.isEmpty()) {
					playerdrops.add(new ItemEntity(world, pvec.x, pvec.y+1, pvec.z, slot.copy()));
					slot.setCount(0);
				}
			}
			
			Iterator<ItemStack> it = player.getAllSlots().iterator();
			while (it.hasNext()) {
				ItemStack next = it.next();
				if (!next.isEmpty()) {
					playerdrops.add(new ItemEntity(world, pvec.x, pvec.y+1, pvec.z, next.copy()));
					next.setCount(0);
				}
			}
			
			ForgeHooks.onLivingDrops((LivingEntity)player, e.getSource(), playerdrops, 0, true);
			
			if (ConfigHandler.GENERAL.dropItemsOnDeath.get()) {
				for (ItemEntity drop : playerdrops) {
					world.addFreshEntity(drop);
				}
			}
		}
		
		Date now = new Date();
		death_times.put(player, now);
		
		StringFunctions.sendMessage(player, ConfigHandler.GENERAL.onDeathMessage.get(), TextFormatting.DARK_RED);
	}
	
	@SubscribeEvent
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent e) {
		PlayerEntity player = e.getPlayer();
		World world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (death_times.containsKey(player)) {
			Util.respawnPlayer(world, player);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
		PlayerEntity player = e.getPlayer();
		World world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (player.isSpectator() && !death_times.containsKey(player)) {
			Util.respawnPlayer(world, player);
		}
	}
}
