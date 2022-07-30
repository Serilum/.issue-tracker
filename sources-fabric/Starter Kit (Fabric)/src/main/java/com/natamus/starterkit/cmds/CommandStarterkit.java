/*
 * This is the latest source code of Starter Kit.
 * Minecraft version: 1.19.1, mod version: 3.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Starter Kit ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.starterkit.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective_fabric.functions.PlayerFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.starterkit.util.Util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.ChatFormatting;

public class CommandStarterkit {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("starterkit").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.then(Commands.literal("set")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Player player;
				try {
					player = source.getPlayerOrException();
				}
				catch (CommandSyntaxException ex) {
					StringFunctions.sendMessage(source, "This command can only be executed as a player in-game.", ChatFormatting.RED);
					return 1;
				}
				
				String newskconfig = PlayerFunctions.getPlayerGearString(player);
				if (!Util.processNewGearString(newskconfig)) {
					StringFunctions.sendMessage(source, "Something went wrong while processing the new starterkit config.", ChatFormatting.RED);
					return 0;
				}
				
				StringFunctions.sendMessage(source, "Starter Kit config updated. All new players will now receive your current inventory.", ChatFormatting.DARK_GREEN);
				return 1;
			}))
		);
    }
}
