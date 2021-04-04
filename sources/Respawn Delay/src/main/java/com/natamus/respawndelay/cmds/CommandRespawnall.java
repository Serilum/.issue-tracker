/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.16.5, mod version: 2.3.
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
import com.natamus.collective.functions.StringFunctions;
import com.natamus.respawndelay.events.RespawningEvent;
import com.natamus.respawndelay.util.Util;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;

public class CommandRespawnall {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("respawnall").requires((iCommandSender) -> iCommandSender.hasPermissionLevel(2))
			.executes((command) -> {
				CommandSource source = command.getSource();
				
				int amountrespawned = 0;
				Set<PlayerEntity> spectating_players = RespawningEvent.death_times.keySet();
				Iterator<PlayerEntity> it = spectating_players.iterator();
				while (it.hasNext()) {
					PlayerEntity nextplayer = it.next();
					Util.respawnPlayer(nextplayer.getEntityWorld(), nextplayer);
					amountrespawned += 1;
				}
				
				String s = "";
				if (amountrespawned > 1) {
					s = "s";
				}
				
				StringFunctions.sendMessage(source, "Successfully made " + amountrespawned + " player" + s + " respawn.", TextFormatting.DARK_GREEN);
				return 1;
			})
		);
    }
}
