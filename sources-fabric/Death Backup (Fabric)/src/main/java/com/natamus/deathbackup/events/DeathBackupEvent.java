/*
 * This is the latest source code of Death Backup.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
		
		if (ConfigHandler.sendBackupReminderMessageToThoseWithAccessOnDeath.getValue()) {
			if (player.hasPermissions(2)) {
				StringFunctions.sendMessage(player, ConfigHandler.backupReminderMessage.getValue(), ChatFormatting.DARK_GRAY);
			}
		}
	}
}
