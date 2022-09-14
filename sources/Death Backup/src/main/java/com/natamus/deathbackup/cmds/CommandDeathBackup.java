/*
 * This is the latest source code of Death Backup.
 * Minecraft version: 1.19.2, mod version: 1.8.
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

package com.natamus.deathbackup.cmds;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.DateFunctions;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.deathbackup.util.Util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class CommandDeathBackup {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("deathbackup").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				StringFunctions.sendMessage(source, "Death Backup usage:", ChatFormatting.DARK_GREEN);
				StringFunctions.sendMessage(source, "/deathbackup list - Lists all available backups.", ChatFormatting.DARK_GREEN);
				StringFunctions.sendMessage(source, "/deathbackup load <index> - Loads the backup with <index> from '/deathbackup list'. Index 0 is the last death.", ChatFormatting.DARK_GREEN);
				return 1;
			})
			.then(Commands.literal("list")
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
				
				Level world = player.getCommandSenderWorld();
				if (world.isClientSide) {
					StringFunctions.sendMessage(source, "[Error] The world is not remote, unable to load death backup.", ChatFormatting.RED);
					return 1;
				}
				
				if (world instanceof ServerLevel == false) {
					StringFunctions.sendMessage(source, "[Error] Cannot find the world's server, unable to load death backup.", ChatFormatting.RED);
					return 1;
				}
				
				String playername = player.getName().getString().toLowerCase();
				ServerLevel serverworld = (ServerLevel)world;
				
				List<String> backups = Util.getListOfBackups(serverworld, playername);

				StringFunctions.sendMessage(source, "Last Death Backups: (<index>: <date>)", ChatFormatting.DARK_GREEN, true);
				
				int index = 0;
				for (String ymdhis : backups) {
					StringFunctions.sendMessage(source, " " + index + ": " + DateFunctions.ymdhisToReadable(ymdhis), ChatFormatting.DARK_GREEN);
					index += 1;
					if (index == 10) {
						break;
					}
				}
				
				StringFunctions.sendMessage(source, "Load the backup with '/deathbackup load <index>'.", ChatFormatting.YELLOW);
				return 1;
			}))
			.then(Commands.literal("load")
			.then(Commands.argument("backup_index", IntegerArgumentType.integer())
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
				
				Level world = player.getCommandSenderWorld();
				if (world.isClientSide) {
					StringFunctions.sendMessage(source, "[Error] The world is not remote, unable to load death backup.", ChatFormatting.RED);
					return 1;
				}
				
				if (world instanceof ServerLevel == false) {
					StringFunctions.sendMessage(source, "[Error] Cannot find the world's server, unable to load death backup.", ChatFormatting.RED);
					return 1;
				}
				
				String playername = player.getName().getString().toLowerCase();
				ServerLevel serverworld = (ServerLevel)world;
				
				List<String> backups = Util.getListOfBackups(serverworld, playername);
				
				Integer amount = IntegerArgumentType.getInteger(command, "backup_index");
				if (amount < 0 || amount >= backups.size()) {
					StringFunctions.sendMessage(source, "The index " + amount + " is invalid.", ChatFormatting.RED);
					return 0;
				}
				
				String backupfilename = backups.get(amount);
				String gearstring = Util.getGearStringFromFile(serverworld, playername, backupfilename);
				if (gearstring == "") {
					StringFunctions.sendMessage(source, "[Error] Unable to read the backup file.", ChatFormatting.RED);
					return 0;
				}

				PlayerFunctions.setPlayerGearFromString(player, gearstring);
				StringFunctions.sendMessage(source, "Successfully loaded the death backup from " + DateFunctions.ymdhisToReadable(backupfilename) + " into your inventory.", ChatFormatting.DARK_GREEN);
				return 1;
			})))
		);
    }
}
