/*
 * This is the latest source code of Omega Mute.
 * Minecraft version: 1.16.5, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Omega Mute ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.omegamute.cmds;

import java.util.HashMap;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.omegamute.events.MuteEvent;
import com.natamus.omegamute.util.Util;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;

public class CommandOmega {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("omegamute")
			.then(Commands.literal("reload")
			.executes((command) -> {
				CommandSource source = command.getSource();
				StringFunctions.sendMessage(source, "Reloading the omega mute soundmap file now.", TextFormatting.DARK_GREEN);
				try {
					if (Util.loadSoundFile()) {
						StringFunctions.sendMessage(source, "New soundmap changes successfully loaded.", TextFormatting.DARK_GREEN);
					}
					else {
						StringFunctions.sendMessage(source, "No soundmap found, a new one has been generated.", TextFormatting.DARK_GREEN);
					}
				} catch (Exception ex) {
					StringFunctions.sendMessage(source, "Something went wrong while loading the soundmap file.", TextFormatting.RED);
				}
				return 1;
			}))
			.then(Commands.literal("query")
			.executes((command) -> {
				CommandSource source = command.getSource();
				
				HashMap<String, Integer> mutedsounds = Util.getMutedSounds();
				if (mutedsounds.size() > 0) {
					String combined = "";
					for (String soundname : mutedsounds.keySet()) {
						if (combined != "") {
							combined += ", ";
						}
						Integer mutedvalue = mutedsounds.get(soundname);
						if (mutedvalue > 0) {
							combined += soundname + "(" + mutedvalue + ")";
						}
						else {
							combined += soundname;
						}
					}
					
					StringFunctions.sendMessage(source, "The following sound events are currently muted:", TextFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, TextFormatting.YELLOW);
				}
				else {
					StringFunctions.sendMessage(source, "There are currently no sound events muted.", TextFormatting.DARK_GREEN);
				}
				
				return 1;
			}))
			.then(Commands.literal("listen").requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity)
			.executes((command) -> {
				CommandSource source = command.getSource();
				PlayerEntity player = (PlayerEntity)source.asPlayer();
				if (MuteEvent.listeningplayers.contains(player)) {
					MuteEvent.listeningplayers.remove(player);
					StringFunctions.sendMessage(player, "You have stopped listening to the active sounds. To toggle it on use '/omegamute listen' again.", TextFormatting.DARK_GREEN);
				}
				else {
					MuteEvent.listeningplayers.add(player);
					StringFunctions.sendMessage(player, "You are now listening to the active sounds. To toggle it off use '/omegamute listen' again.", TextFormatting.DARK_GREEN);
				}
				
				return 1;
			}))
			.then(Commands.literal("mute")
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				CommandSource source = command.getSource();
				
				String wildcard = StringArgumentType.getString(command, "string-contains");
				List<String> muted = Util.muteWildcard(wildcard, 0);
				if (muted.size() > 0) {
					String combined = String.join(", ", muted);
					StringFunctions.sendMessage(source, "By using the wildcard string '" + wildcard + "', the following " + muted.size() + " sound events have been muted:", TextFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, TextFormatting.YELLOW);
					StringFunctions.sendMessage(source, "The soundmap file has been updated.", TextFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(source, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", TextFormatting.RED);
				}
				
				return 1;
			})))
			.then(Commands.literal("cull")
			.then(Commands.argument("cull-time", IntegerArgumentType.integer(0, 3600))
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				CommandSource source = command.getSource();
				
				int culltime = IntegerArgumentType.getInteger(command, "cull-time");
				String wildcard = StringArgumentType.getString(command, "string-contains");
				
				List<String> muted = Util.muteWildcard(wildcard, culltime);
				if (muted.size() > 0) {
					String combined = String.join(", ", muted);
					StringFunctions.sendMessage(source, "By using the wildcard string '" + wildcard + "', the following " + muted.size() + " sound events have been muted with a cull-time of " + culltime + ":", TextFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, TextFormatting.YELLOW);
					StringFunctions.sendMessage(source, "The soundmap file has been updated.", TextFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(source, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", TextFormatting.RED);
				}
				
				return 1;
			}))))
			.then(Commands.literal("unmute")
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				CommandSource source = command.getSource();
				
				String wildcard = StringArgumentType.getString(command, "string-contains");
				List<String> unmuted = Util.unmuteWildcard(wildcard);
				if (unmuted.size() > 0) {
					String combined = String.join(", ", unmuted);
					StringFunctions.sendMessage(source, "By using the wildcard string '" + wildcard + "', the following " + unmuted.size() + " sound events have been unmuted:", TextFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, TextFormatting.YELLOW);
					StringFunctions.sendMessage(source, "The soundmap file has been updated.", TextFormatting.DARK_GREEN);}
				else {
					StringFunctions.sendMessage(source, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", TextFormatting.RED);
				}
				
				return 1;
			})))
		);
    }
}