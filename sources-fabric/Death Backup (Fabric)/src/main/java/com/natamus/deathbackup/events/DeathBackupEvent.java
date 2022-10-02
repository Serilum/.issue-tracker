/*
 * This is the latest source code of Death Backup.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.deathbackup.events;

import com.natamus.collective_fabric.functions.DateFunctions;
import com.natamus.collective_fabric.functions.PlayerFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.deathbackup.config.ConfigHandler;
import com.natamus.deathbackup.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class DeathBackupEvent {

	public static void onPlayerDeath(ServerLevel serverworld, ServerPlayer player) {
		String playername = player.getName().getString().toLowerCase();
		
		String gearstring = PlayerFunctions.getPlayerGearString(player);
		if (gearstring == "") {
			return;
		}
		
		String nowstring = DateFunctions.getNowInYmdhis();
		Util.writeGearStringToFile(serverworld, playername, nowstring, gearstring);
		
		if (ConfigHandler.sendBackupReminderMessageToThoseWithAccessOnDeath) {
			if (player.hasPermissions(2)) {
				StringFunctions.sendMessage(player, ConfigHandler.backupReminderMessage, ChatFormatting.DARK_GRAY);
			}
		}
	}
}
