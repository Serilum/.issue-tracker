/*
 * This is the latest source code of Just Player Heads.
 * Minecraft version: 1.19.3, mod version: 2.6.
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
		if (!ConfigHandler.playerDropsHeadOnDeath) {
			return;
		}
		
		double num = GlobalVariables.random.nextDouble();
		if (num > ConfigHandler.playerHeadDropChance) {
			return;
		}
		
		String name = player.getName().getString();
		
		ItemStack head = null;
		if (ConfigHandler.enablePlayerHeadCaching) {
			if (Variables.headcache.containsKey(name.toLowerCase())) {
				head = Variables.headcache.get(name.toLowerCase());
			}
		}
		
		if (head == null) {
			head = HeadFunctions.getPlayerHead(name, 1);
			
			if (head != null && ConfigHandler.enablePlayerHeadCaching) {
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
