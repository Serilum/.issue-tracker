/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.18.x, mod version: 2.5.
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

package com.natamus.respawndelay.util;

import com.natamus.collective_fabric.functions.PlayerFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
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
		if (ConfigHandler.respawnAtWorldSpawn.getValue()) {
			BlockPos spawnpos = PlayerFunctions.getSpawnPoint(world, serverplayer);
			serverplayer.teleportTo(spawnpos.getX(), spawnpos.getY(), spawnpos.getZ());
		}
		
		StringFunctions.sendMessage(serverplayer, ConfigHandler.onRespawnMessage.getValue(), ChatFormatting.DARK_GREEN);
	}
}
