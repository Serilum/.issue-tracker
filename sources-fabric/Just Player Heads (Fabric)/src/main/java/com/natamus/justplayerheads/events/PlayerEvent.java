/*
 * This is the latest source code of Just Player Heads.
 * Minecraft version: 1.19.1, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.justplayerheads.events;

import com.natamus.collective_fabric.data.GlobalVariables;
import com.natamus.collective_fabric.functions.HeadFunctions;
import com.natamus.justplayerheads.config.ConfigHandler;
import com.natamus.justplayerheads.util.Variables;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class PlayerEvent {
	public static void onPlayerDeath(ServerLevel world, ServerPlayer player) {
		if (!ConfigHandler.playerDropsHeadOnDeath.getValue()) {
			return;
		}
		
		double num = GlobalVariables.random.nextDouble();
		if (num > ConfigHandler.playerHeadDropChance.getValue()) {
			return;
		}
		
		String name = player.getName().getString();
		
		ItemStack head = null;
		if (ConfigHandler.enablePlayerHeadCaching.getValue()) {
			if (Variables.headcache.containsKey(name.toLowerCase())) {
				head = Variables.headcache.get(name.toLowerCase());
			}
		}
		
		if (head == null) {
			head = HeadFunctions.getPlayerHead(name, 1);
			
			if (head != null && ConfigHandler.enablePlayerHeadCaching.getValue()) {
				ItemStack cachehead = head.copy();
				
				Variables.headcache.put(name.toLowerCase(), cachehead);
			}
		}
		
		if (head == null) {
			return;
		}
		
		player.spawnAtLocation(head, 1);
	}
}
