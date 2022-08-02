/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.19.1, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.respawndelay.cmds;

import java.util.Iterator;
import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.respawndelay.events.RespawningEvent;
import com.natamus.respawndelay.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class CommandRespawnall {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("respawnall").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				int amountrespawned = 0;
				Set<Player> spectating_players = RespawningEvent.death_times.keySet();
				Iterator<Player> it = spectating_players.iterator();
				while (it.hasNext()) {
					Player nextplayer = it.next();
					Util.respawnPlayer(nextplayer.getCommandSenderWorld(), (ServerPlayer)nextplayer);
					amountrespawned += 1;
				}
				
				String s = "";
				if (amountrespawned > 1) {
					s = "s";
				}
				
				StringFunctions.sendMessage(source, "Successfully made " + amountrespawned + " player" + s + " respawn.", ChatFormatting.DARK_GREEN);
				return 1;
			})
		);
    }
}
