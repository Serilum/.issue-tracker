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
