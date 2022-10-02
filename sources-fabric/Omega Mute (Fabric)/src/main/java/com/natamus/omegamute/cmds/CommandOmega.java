/*
 * This is the latest source code of Omega Mute.
 * Minecraft version: 1.19.2, mod version: 2.7.
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

package com.natamus.omegamute.cmds;

import java.util.HashMap;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.omegamute.events.MuteEvent;
import com.natamus.omegamute.util.Util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.ChatFormatting;

public class CommandOmega {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("omegamute")
			.then(Commands.literal("reload")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				StringFunctions.sendMessage(source, "Reloading the omega mute soundmap file now.", ChatFormatting.DARK_GREEN);
				try {
					if (Util.loadSoundFile()) {
						StringFunctions.sendMessage(source, "New soundmap changes successfully loaded.", ChatFormatting.DARK_GREEN);
					}
					else {
						StringFunctions.sendMessage(source, "No soundmap found, a new one has been generated.", ChatFormatting.DARK_GREEN);
					}
				} catch (Exception ex) {
					StringFunctions.sendMessage(source, "Something went wrong while loading the soundmap file.", ChatFormatting.RED);
				}
				return 1;
			}))
			.then(Commands.literal("query")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
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
					
					StringFunctions.sendMessage(source, "The following sound events are currently muted:", ChatFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, ChatFormatting.YELLOW);
				}
				else {
					StringFunctions.sendMessage(source, "There are currently no sound events muted.", ChatFormatting.DARK_GREEN);
				}
				
				return 1;
			}))
			.then(Commands.literal("listen").requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Player player = (Player)source.getPlayerOrException();
				if (MuteEvent.listeningplayers.contains(player)) {
					MuteEvent.listeningplayers.remove(player);
					StringFunctions.sendMessage(player, "You have stopped listening to the active sounds. To toggle it on use '/omegamute listen' again.", ChatFormatting.DARK_GREEN);
				}
				else {
					MuteEvent.listeningplayers.add(player);
					StringFunctions.sendMessage(player, "You are now listening to the active sounds. To toggle it off use '/omegamute listen' again.", ChatFormatting.DARK_GREEN);
				}
				
				return 1;
			}))
			.then(Commands.literal("mute")
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				String wildcard = StringArgumentType.getString(command, "string-contains");
				List<String> muted = Util.muteWildcard(wildcard, 0);
				if (muted.size() > 0) {
					String combined = String.join(", ", muted);
					StringFunctions.sendMessage(source, "By using the wildcard string '" + wildcard + "', the following " + muted.size() + " sound events have been muted:", ChatFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, ChatFormatting.YELLOW);
					StringFunctions.sendMessage(source, "The soundmap file has been updated.", ChatFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(source, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", ChatFormatting.RED);
				}
				
				return 1;
			})))
			.then(Commands.literal("cull")
			.then(Commands.argument("cull-time", IntegerArgumentType.integer(0, 3600))
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				int culltime = IntegerArgumentType.getInteger(command, "cull-time");
				String wildcard = StringArgumentType.getString(command, "string-contains");
				
				List<String> muted = Util.muteWildcard(wildcard, culltime);
				if (muted.size() > 0) {
					String combined = String.join(", ", muted);
					StringFunctions.sendMessage(source, "By using the wildcard string '" + wildcard + "', the following " + muted.size() + " sound events have been muted with a cull-time of " + culltime + ":", ChatFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, ChatFormatting.YELLOW);
					StringFunctions.sendMessage(source, "The soundmap file has been updated.", ChatFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(source, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", ChatFormatting.RED);
				}
				
				return 1;
			}))))
			.then(Commands.literal("unmute")
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				String wildcard = StringArgumentType.getString(command, "string-contains");
				List<String> unmuted = Util.unmuteWildcard(wildcard);
				if (unmuted.size() > 0) {
					String combined = String.join(", ", unmuted);
					StringFunctions.sendMessage(source, "By using the wildcard string '" + wildcard + "', the following " + unmuted.size() + " sound events have been unmuted:", ChatFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, ChatFormatting.YELLOW);
					StringFunctions.sendMessage(source, "The soundmap file has been updated.", ChatFormatting.DARK_GREEN);}
				else {
					StringFunctions.sendMessage(source, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", ChatFormatting.RED);
				}
				
				return 1;
			})))
		);
    }
}