/*
 * This is the latest source code of Save and Load Inventories.
 * Minecraft version: 1.16.5, mod version: 1.5.
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

public class CommandLoadinventory {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("loadinventory").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.then(Commands.argument("inventory-name", StringArgumentType.word())
			.executes((command) -> {
				return loadInventory(command);
			}))
			.then(Commands.argument("inventory-name", StringArgumentType.word())
			.then(Commands.argument("player-name", StringArgumentType.word())
			.executes((command) -> {
				return loadInventoryForPlayerName(command);
			})))
		);
    	dispatcher.register(Commands.literal("li").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.then(Commands.argument("inventory-name", StringArgumentType.word())
			.executes((command) -> {
				return loadInventory(command);
			}))
			.then(Commands.argument("inventory-name", StringArgumentType.word())
			.then(Commands.argument("player-name", StringArgumentType.word())
			.executes((command) -> {
				return loadInventoryForPlayerName(command);
			})))
		);
    }
    
    private static int loadInventory(CommandContext<CommandSource> command) {
    	CommandSource source = command.getSource();
    	
    	PlayerEntity player;
		try {
			player = source.getPlayerOrException();
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
		
		String gearstring = Util.getGearStringFromFile(inventoryname);
		if (gearstring == "") {
			StringFunctions.sendMessage(source, "Unable to load the content of the inventory with the name '" + inventoryname + "'.", TextFormatting.RED);
			return 0;					
		}
		
		PlayerFunctions.setPlayerGearFromString(player, gearstring);
		StringFunctions.sendMessage(source, "Successfully loaded '" + inventoryname + "' to your inventory.", TextFormatting.DARK_GREEN);
		return 1;
    }
    
    private static int loadInventoryForPlayerName(CommandContext<CommandSource> command) {
    	CommandSource source = command.getSource();
    	
    	PlayerEntity player;
		try {
			player = source.getPlayerOrException();
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
		
		String targetname = StringArgumentType.getString(command, "player-name").toLowerCase();
		PlayerEntity target = PlayerFunctions.matchPlayer(player, targetname.toLowerCase());
		if (target == null) {
			StringFunctions.sendMessage(source, "Unable to find an online player with the name '" + targetname + "'.", TextFormatting.RED);
			return 0;			
		}
		
		String gearstring = Util.getGearStringFromFile(inventoryname);
		if (gearstring == "") {
			StringFunctions.sendMessage(source, "Unable to load the content of the inventory with the name '" + inventoryname + "'.", TextFormatting.RED);
			return 0;					
		}
		
		PlayerFunctions.setPlayerGearFromString(target, gearstring);
		StringFunctions.sendMessage(source, "Successfully loaded '" + inventoryname + "' to the inventory of '" + target.getName().getString() + "'.", TextFormatting.DARK_GREEN);
		StringFunctions.sendMessage(target, "Your inventory has been replaced with the preset named '" + inventoryname + "'.", TextFormatting.DARK_GREEN);
		return 1;
    }
}
