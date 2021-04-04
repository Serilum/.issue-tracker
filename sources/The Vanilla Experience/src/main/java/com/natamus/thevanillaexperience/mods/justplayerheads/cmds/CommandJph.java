/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.mods.justplayerheads.cmds;

import java.util.HashMap;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.HeadFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.justplayerheads.config.JustPlayerHeadsConfigHandler;
import com.natamus.thevanillaexperience.mods.justplayerheads.util.JustPlayerHeadsVariables;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class CommandJph {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("jph")
    			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity && iCommandSender.hasPermissionLevel(2))
    			.then(Commands.argument("name", StringArgumentType.word())
    			.then(Commands.argument("amount", IntegerArgumentType.integer(1, 64))
    			.executes((command) -> {
		    		Integer amount = 1;
	    			Integer specifiedamount = IntegerArgumentType.getInteger(command, "amount");
	    			if (specifiedamount > 1 && specifiedamount <= 64) {
	    				amount = specifiedamount;		    			
	    			}
		    		
		    		processJph(command, amount);
		    		return 1;
		        })))
    			.then(Commands.argument("name", StringArgumentType.word())
    			.executes((command) -> {
    				processJph(command, 1);
    				return 1;
    			}))
    			.then(Commands.literal("cache")
    			.executes((command) -> {
    				CommandSource source = command.getSource();
    				
    				JustPlayerHeadsVariables.headcache = new HashMap<String, ItemStack>();
    				
    				StringFunctions.sendMessage(source, "The player head texture cache has been emptied.", TextFormatting.DARK_GREEN);
    				return 1;
    			}))
    			.executes((command) -> {
    				CommandSource source = command.getSource();

    				StringFunctions.sendMessage(source, "Allows you to get the head of a player.", TextFormatting.DARK_GREEN);
    				StringFunctions.sendMessage(source, " Usage: /jph playerName (amount)", TextFormatting.DARK_GREEN);
    				return 1;
    			})
    		);
    }
    
    public static void processJph(CommandContext<CommandSource> command, Integer amount) throws CommandSyntaxException {
    	CommandSource source = command.getSource();
		
		String target = StringArgumentType.getString(command, "name");
		
		ItemStack head = null;
		if (JustPlayerHeadsConfigHandler.GENERAL.enablePlayerHeadCaching.get()) {
			if (JustPlayerHeadsVariables.headcache.containsKey(target.toLowerCase())) {
				head = JustPlayerHeadsVariables.headcache.get(target.toLowerCase());
				head.setCount(amount);
			}
		}
		
		if (head == null) {
			head = HeadFunctions.getPlayerHead(target, amount);
			
			if (head != null && JustPlayerHeadsConfigHandler.GENERAL.enablePlayerHeadCaching.get()) {
				ItemStack cachehead = head.copy();
				cachehead.setCount(1);
				
				JustPlayerHeadsVariables.headcache.put(target.toLowerCase(), cachehead);
			}
		}
		
		String message = "";
		if (head == null) {
			message = "Unable to generate the player head. Either the player '" + target + "' does not exist or the Mojang API server has had too many requests in a short period of time.";
			StringFunctions.sendMessage(source, message, TextFormatting.DARK_RED);
		}
		else {
			message = "Succesfully generated the head of the player '" + target + "'.";
			StringFunctions.sendMessage(source, message, TextFormatting.DARK_GREEN);
			
			PlayerEntity player = source.asPlayer();
			player.entityDropItem(head, 1);
		}
    }
}