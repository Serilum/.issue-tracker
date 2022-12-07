/*
 * This is the latest source code of Save and Load Inventories.
 * Minecraft version: 1.19.3, mod version: 2.5.
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

package com.natamus.saveandloadinventories.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective_fabric.functions.PlayerFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.saveandloadinventories.util.Util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.ChatFormatting;

public class CommandSaveinventory {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("saveinventory").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.then(Commands.argument("inventory-name", StringArgumentType.word())
			.executes((command) -> {
				return saveinventory(command);
			}))
		);
    	dispatcher.register(Commands.literal("si").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.then(Commands.argument("inventory-name", StringArgumentType.word())
			.executes((command) -> {
				return saveinventory(command);
			}))
		);
    }
    
    private static int saveinventory(CommandContext<CommandSourceStack> command) {
		CommandSourceStack source = command.getSource();
		Player player;
		try {
			player = source.getPlayerOrException();
		}
		catch (CommandSyntaxException ex) {
			StringFunctions.sendMessage(source, "This command can only be executed as a player in-game.", ChatFormatting.RED);
			return 1;
		}
		
		String inventoryname = StringArgumentType.getString(command, "inventory-name").toLowerCase();
		if (inventoryname.trim() == "") {
			StringFunctions.sendMessage(source, "The inventory name '" + inventoryname + "' is invalid.", ChatFormatting.RED);
			return 0;
		}
		
		String gearstring = PlayerFunctions.getPlayerGearString(player);
		if (StringFunctions.sequenceCount(gearstring, "\n") < 40) {
			StringFunctions.sendMessage(source, "Something went wrong while generating the save file content for your inventory.", ChatFormatting.RED);
			return 0;					
		}
		
		if (!Util.writeGearStringToFile(inventoryname, gearstring)) {
			StringFunctions.sendMessage(source, "Something went wrong while saving the content of your inventory as '" + inventoryname + "'.", ChatFormatting.RED);
			return 0;							
		}
		
		StringFunctions.sendMessage(source, "Successfully saved your inventory as '" + inventoryname + "'.", ChatFormatting.DARK_GREEN);
		StringFunctions.sendMessage(source, "You can load it with the command '/loadinventory " + inventoryname + "'.", ChatFormatting.DARK_GREEN);
		return 1;
    }
}
