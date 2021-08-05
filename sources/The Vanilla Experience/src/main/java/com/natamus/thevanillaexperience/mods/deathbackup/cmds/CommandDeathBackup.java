/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.deathbackup.cmds;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.DateFunctions;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.deathbackup.util.DeathBackupUtil;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CommandDeathBackup {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("deathbackup").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				CommandSource source = command.getSource();
				StringFunctions.sendMessage(source, "Death Backup usage:", TextFormatting.DARK_GREEN);
				StringFunctions.sendMessage(source, "/deathbackup list - Lists all available backups.", TextFormatting.DARK_GREEN);
				StringFunctions.sendMessage(source, "/deathbackup load <index> - Loads the backup with <index> from '/deathbackup list'. Index 0 is the last death.", TextFormatting.DARK_GREEN);
				return 1;
			})
			.then(Commands.literal("list")
			.executes((command) -> {
				CommandSource source = command.getSource();
				PlayerEntity player;
				try {
					player = source.getPlayerOrException();
				}
				catch (CommandSyntaxException ex) {
					StringFunctions.sendMessage(source, "This command can only be executed as a player in-game.", TextFormatting.RED);
					return 1;
				}
				
				World world = player.getCommandSenderWorld();
				if (world.isClientSide) {
					StringFunctions.sendMessage(source, "[Error] The world is not remote, unable to load death backup.", TextFormatting.RED);
					return 1;
				}
				
				if (world instanceof ServerWorld == false) {
					StringFunctions.sendMessage(source, "[Error] Cannot find the world's server, unable to load death backup.", TextFormatting.RED);
					return 1;
				}
				
				String playername = player.getName().getString().toLowerCase();
				ServerWorld serverworld = (ServerWorld)world;
				
				List<String> backups = DeathBackupUtil.getListOfBackups(serverworld, playername);

				StringFunctions.sendMessage(source, "Last Death Backups: (<index>: <date>)", TextFormatting.DARK_GREEN, true);
				
				int index = 0;
				for (String ymdhis : backups) {
					StringFunctions.sendMessage(source, " " + index + ": " + DateFunctions.ymdhisToReadable(ymdhis), TextFormatting.DARK_GREEN);
					index += 1;
					if (index == 10) {
						break;
					}
				}
				
				StringFunctions.sendMessage(source, "Load the backup with '/deathbackup load <index>'.", TextFormatting.YELLOW);
				return 1;
			}))
			.then(Commands.literal("load")
			.then(Commands.argument("backup_index", IntegerArgumentType.integer())
			.executes((command) -> {
				CommandSource source = command.getSource();
				PlayerEntity player;
				try {
					player = source.getPlayerOrException();
				}
				catch (CommandSyntaxException ex) {
					StringFunctions.sendMessage(source, "This command can only be executed as a player in-game.", TextFormatting.RED);
					return 1;
				}
				
				World world = player.getCommandSenderWorld();
				if (world.isClientSide) {
					StringFunctions.sendMessage(source, "[Error] The world is not remote, unable to load death backup.", TextFormatting.RED);
					return 1;
				}
				
				if (world instanceof ServerWorld == false) {
					StringFunctions.sendMessage(source, "[Error] Cannot find the world's server, unable to load death backup.", TextFormatting.RED);
					return 1;
				}
				
				String playername = player.getName().getString().toLowerCase();
				ServerWorld serverworld = (ServerWorld)world;
				
				List<String> backups = DeathBackupUtil.getListOfBackups(serverworld, playername);
				
				Integer amount = IntegerArgumentType.getInteger(command, "backup_index");
				if (amount < 0 || amount >= backups.size()) {
					StringFunctions.sendMessage(source, "The index " + amount + " is invalid.", TextFormatting.RED);
					return 0;
				}
				
				String backupfilename = backups.get(amount);
				String gearstring = DeathBackupUtil.getGearStringFromFile(serverworld, playername, backupfilename);
				if (gearstring == "") {
					StringFunctions.sendMessage(source, "[Error] Unable to read the backup file.", TextFormatting.RED);
					return 0;
				}

				PlayerFunctions.setPlayerGearFromString(player, gearstring);
				StringFunctions.sendMessage(source, "Successfully loaded the death backup from " + DateFunctions.ymdhisToReadable(backupfilename) + " into your inventory.", TextFormatting.DARK_GREEN);
				return 1;
			})))
		);
    }
}
