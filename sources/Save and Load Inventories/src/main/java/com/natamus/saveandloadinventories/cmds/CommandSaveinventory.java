/*
 * This is the latest source code of Save and Load Inventories.
 * Minecraft version: 1.16.5, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Save and Load Inventories ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.saveandloadinventories.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.saveandloadinventories.util.Util;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;

public class CommandSaveinventory {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("saveinventory").requires((iCommandSender) -> iCommandSender.hasPermissionLevel(2))
			.then(Commands.argument("inventory-name", StringArgumentType.word())
			.executes((command) -> {
				return saveinventory(command);
			}))
		);
    	dispatcher.register(Commands.literal("si").requires((iCommandSender) -> iCommandSender.hasPermissionLevel(2))
			.then(Commands.argument("inventory-name", StringArgumentType.word())
			.executes((command) -> {
				return saveinventory(command);
			}))
		);
    }
    
    private static int saveinventory(CommandContext<CommandSource> command) {
		CommandSource source = command.getSource();
		PlayerEntity player;
		try {
			player = source.asPlayer();
		}
		catch (CommandSyntaxException ex) {
			StringFunctions.sendMessage(source, "This command can only be executed as a player in-game.", TextFormatting.RED);
			return 1;
		}
		
		String inventoryname = StringArgumentType.getString(command, "inventory-name").toLowerCase();
		if (inventoryname.trim() == "") {
			StringFunctions.sendMessage(source, "The inventory name '" + inventoryname + "' is invalid.", TextFormatting.RED);
			return 0;
		}
		
		String gearstring = PlayerFunctions.getPlayerGearString(player);
		if (StringFunctions.sequenceCount(gearstring, "\n") < 40) {
			StringFunctions.sendMessage(source, "Something went wrong while generating the save file content for your inventory.", TextFormatting.RED);
			return 0;					
		}
		
		if (!Util.writeGearStringToFile(inventoryname, gearstring)) {
			StringFunctions.sendMessage(source, "Something went wrong while saving the content of your inventory as '" + inventoryname + "'.", TextFormatting.RED);
			return 0;							
		}
		
		StringFunctions.sendMessage(source, "Successfully saved your inventory as '" + inventoryname + "'.", TextFormatting.DARK_GREEN);
		StringFunctions.sendMessage(source, "You can load it with the command '/loadinventory " + inventoryname + "'.", TextFormatting.DARK_GREEN);
		return 1;
    }
}
