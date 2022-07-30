/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.19.1, mod version: 3.0.
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

import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.respawndelay.config.ConfigHandler;
import com.natamus.respawndelay.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RespawningEvent {
	public static HashMap<Player, Date> death_times = new HashMap<Player, Date>();
	
	public static void onPlayerTick(ServerLevel world, ServerPlayer player) {
		if (player.tickCount % 20 != 0) {
			return;
		}
		
		if (!player.isSpectator()) {
			return;
		}
		
		if (!death_times.containsKey(player)) {
			return;
		}
		
		int respawndelay = ConfigHandler.respawnDelayInSeconds.getValue();
		if (respawndelay < 0) {
			return;
		}
		
		Date now = new Date();
		Date timedied = death_times.get(player); 
		
		long ms = (now.getTime()-timedied.getTime());
		if (ms > respawndelay*1000) {
			Util.respawnPlayer(world, (ServerPlayer)player);
			return;
		}
		
		int seconds = respawndelay - (int)(ms/1000);
		String waitingmessage = ConfigHandler.waitingForRespawnMessage.getValue();
		waitingmessage = waitingmessage.replaceAll("<seconds_left>", seconds + "");
		
		StringFunctions.sendMessage(player, waitingmessage, ChatFormatting.GRAY);
	}
	
	public static boolean onPlayerDeath(ServerPlayer player, DamageSource damageSource, float damageAmount) {
		if (player instanceof ServerPlayer == false) {
			return true;
		}
		
		if (ConfigHandler.ignoreAdministratorPlayers.getValue()) {
			if (player.hasPermissions(2)) {
				return true;
			}
		}
		
		if (ConfigHandler.ignoreCreativePlayers.getValue()) {
			if (player.isCreative()) {
				return true;
			}
		}
		
		ServerPlayer serverplayer = (ServerPlayer)player;
		ServerLevel world = (ServerLevel)serverplayer.level;
		
		serverplayer.setGameMode(GameType.SPECTATOR);
		player.setHealth(20);
		player.awardStat(Stats.DEATHS);
		player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
		player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
		player.clearFire();
		
		Vec3 pvec = player.position();
		if (pvec.y() < ConfigHandler.lowestPossibleYCoordinate.getValue()) {
			pvec = new Vec3(pvec.x(), ConfigHandler.lowestPossibleYCoordinate.getValue(), pvec.z());
			player.setDeltaMovement(0, 0, 0);
			player.teleportTo(pvec.x(), pvec.y(), pvec.z());
		}
		
		if (!ConfigHandler.keepItemsOnDeath.getValue()) {
			Collection<ItemEntity> playerdrops = new ArrayList<ItemEntity>();
			
			Inventory inv = player.getInventory();
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
			
			//ForgeHooks.onLivingDrops((LivingEntity)player, e.getSource(), playerdrops, 0, true);
			
			if (ConfigHandler.dropItemsOnDeath.getValue()) {
				for (ItemEntity drop : playerdrops) {
					world.addFreshEntity(drop);
				}
			}
		}
		
		Date now = new Date();
		death_times.put(player, now);
		
		StringFunctions.sendMessage(player, ConfigHandler.onDeathMessage.getValue(), ChatFormatting.DARK_RED);
		return false;
	}
	
	public static void onPlayerLogout(Level world, Player player) {
		if (world.isClientSide) {
			return;
		}
		
		if (player instanceof ServerPlayer == false) {
			return;
		}
		
		if (death_times.containsKey(player)) {
			Util.respawnPlayer(world, (ServerPlayer)player);
		}
	}
	
	public static void onPlayerLogin(Level world, Player player) {
		if (world.isClientSide) {
			return;
		}
		
		if (player instanceof ServerPlayer == false) {
			return;
		}
		
		if (player.isSpectator() && !death_times.containsKey(player)) {
			Util.respawnPlayer(world, (ServerPlayer)player);
		}
	}
}
