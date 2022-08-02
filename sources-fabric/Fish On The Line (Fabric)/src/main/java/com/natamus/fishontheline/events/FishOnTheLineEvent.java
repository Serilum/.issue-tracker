/*
 * This is the latest source code of Fish On The Line.
 * Minecraft version: 1.19.1, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.fishontheline.events;

import java.util.HashMap;
import java.util.List;

import com.natamus.collective_fabric.data.GlobalVariables;
import com.natamus.fishontheline.config.ConfigHandler;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FishOnTheLineEvent {
	private static HashMap<String, Integer> sounddelay = new HashMap<String, Integer>();
	
	public static void onPlayerTick(ServerLevel world, ServerPlayer player) {
		FishingHook fbe = player.fishing;
		if (fbe == null) {
			return;
		}
		
		if (ConfigHandler.mustHoldBellInOffhand.getValue()) {
			ItemStack offhandstack = player.getOffhandItem();
			if (!offhandstack.getItem().equals(Items.BELL)) {
				return;
			}
		}
		
		boolean fishontheline = false;
		int booleancount = 0;
		
		SynchedEntityData datamanager = fbe.getEntityData();
		List<DataItem<?>> entries = datamanager.getAll();
		for (DataItem<?> entry : entries) {
			String entryvalue = entry.getValue().toString();
			if (entryvalue.equalsIgnoreCase("true") || entryvalue.equalsIgnoreCase("false")) {
				if (booleancount >= 1) {
					if (entryvalue.equalsIgnoreCase("true")) {
						fishontheline = true;
					}
				}
				
				booleancount += 1;
			}
		}
		
		if (fishontheline) {
			int delay = 0;
			
			String playername = player.getName().getString();
			if (sounddelay.containsKey(playername)) {
				delay = sounddelay.get(playername);
			}
			
			if (delay == 0) {
				world.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.5F, 0.4F / (GlobalVariables.random.nextFloat() * 0.4F + 0.8F));
				delay = 7;
			}
			else {
				delay -= 1;
			}
			
			sounddelay.put(playername, delay);
		}
	}
}
