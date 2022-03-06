/*
 * This is the latest source code of Just Mob Heads.
 * Minecraft version: 1.18.2, mod version: 5.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Just Mob Heads ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.justmobheads.cmds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.justmobheads.util.HeadData;
import com.natamus.justmobheads.util.MobHeads;
import com.natamus.justmobheads.util.Util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;

public class CommandJmh {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("jmh").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.then(Commands.literal("reload")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				StringFunctions.sendMessage(source, "Reloading head chances config file now.", ChatFormatting.DARK_GREEN);
				try {
					if (Util.generateChanceConfig(HeadData.defaultchances)) {
						StringFunctions.sendMessage(source, "Succesfully loaded! The dropchances have been altered.", ChatFormatting.DARK_GREEN);
					}
					else {
						StringFunctions.sendMessage(source, "Generated new config file. Using the default chances.", ChatFormatting.DARK_GREEN);
					}
				} catch (Exception ex) {
					StringFunctions.sendMessage(source, "Something went wrong while loading the config file.", ChatFormatting.RED);
				}
				return 1;
			}))
			.then(Commands.literal("head")
			.then(Commands.literal("list")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				StringFunctions.sendMessage(source, "You can generate the following mob heads:", ChatFormatting.DARK_GREEN);
				StringFunctions.sendMessage(source, " Usage: /jmh head <name> <amount>:", ChatFormatting.DARK_GREEN);
				
				List<String> mobnames = new ArrayList<String>(HeadData.headdata.keySet());
				Collections.sort(mobnames);
				String mnstr = String.join(", ", mobnames);
				StringFunctions.sendMessage(source, mnstr, ChatFormatting.YELLOW);
				
				return 1;
			})))
			.then(Commands.literal("head")
			.then(Commands.argument("mob-name", StringArgumentType.word())
			.then(Commands.argument("amount", IntegerArgumentType.integer(1, 64))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				String mobname = StringArgumentType.getString(command, "mob-name").toLowerCase();
				Integer amount = IntegerArgumentType.getInteger(command, "amount");
				
				if (!HeadData.headdata.containsKey(mobname)) {
					StringFunctions.sendMessage(source, "The mobname '" + mobname + "' does not exist. You can get a list of all possible heads with:", ChatFormatting.RED);
					StringFunctions.sendMessage(source, " Usage: /jmh head list", ChatFormatting.RED);
					return 1;
				}
				
				Player player;
				try {
					player = source.getPlayerOrException();
				}
				catch (CommandSyntaxException ex) {
					StringFunctions.sendMessage(source, "This command can only be executed as a player in-game.", ChatFormatting.RED);
					return 1;
				}
				
				ItemStack headstack = MobHeads.getMobHead(mobname, amount);
				if (!player.getInventory().add(headstack)) {
					player.drop(headstack, false);
				}
				StringFunctions.sendMessage(source, "Successfully generated " + amount + " " + StringFunctions.capitalizeFirst(mobname.replace("_", " ")) + " heads.", ChatFormatting.DARK_GREEN);
				return 1;
			}))))
		);
    }
}
