/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.19.2, mod version: 2.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.respawndelay.util;

import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.respawndelay.config.ConfigHandler;
import com.natamus.respawndelay.events.RespawningEvent;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

public class Util {
	public static void respawnPlayer(Level world, ServerPlayer serverplayer) {
		if (!PlayerFunctions.respawnPlayer(world, serverplayer)) {
			return;
		}
		
		RespawningEvent.death_times.remove(serverplayer);
		serverplayer.setGameMode(GameType.SURVIVAL);
		if (ConfigHandler.GENERAL.respawnAtWorldSpawn.get()) {
			BlockPos spawnpos = PlayerFunctions.getSpawnPoint(world, serverplayer);
			serverplayer.teleportTo(spawnpos.getX(), spawnpos.getY(), spawnpos.getZ());
		}
		
		StringFunctions.sendMessage(serverplayer, ConfigHandler.GENERAL.onRespawnMessage.get(), ChatFormatting.DARK_GREEN);
	}
}
