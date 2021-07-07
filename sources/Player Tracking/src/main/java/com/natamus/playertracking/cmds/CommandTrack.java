/*
 * This is the latest source code of Player Tracking.
 * Minecraft version: 1.16.5, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Player Tracking ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.playertracking.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.playertracking.Main;
import com.natamus.playertracking.util.Tracking;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class CommandTrack {
	   public static void register(CommandDispatcher<CommandSource> dispatcher) {
	    	dispatcher.register(Commands.literal("track")
	    			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity)
	    			.then(Commands.literal("help")
	    			.executes((command) -> {
	    				PlayerEntity player = command.getSource().getPlayerOrException();
	    				
	    				StringFunctions.sendMessage(player, "Tracking Help Page (1/5)", TextFormatting.DARK_GRAY, true);
	    				StringFunctions.sendMessage(player, "For an introduction to tracking read page 2.", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For information on building a tracker read page 3.", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For explanation on '/track all' read page 4.", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For explanation on '/track [playerName]' read page 5.", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "To select a page: '/track help [page]'", TextFormatting.GRAY);
	    				return 1;
	    			}))
	    			.then(Commands.literal("help")
	    			.then(Commands.argument("page", IntegerArgumentType.integer(1, 5))
	    			.executes((command) -> {
	    				PlayerEntity player = command.getSource().getPlayerOrException();
	    				
	    				Integer page = IntegerArgumentType.getInteger(command, "page");
	    				
	    				if (page == 2) {
    						StringFunctions.sendMessage(player, "Tracking Help Page (2/5) - Introduction", TextFormatting.DARK_GRAY, true);
    						StringFunctions.sendMessage(player, "Tracking is a feature to encourage raiding on a server. Without this it would be near-impossible to find " +
    								"enemy bases legit, and would give hackers an unfair advantage. Tracking is the art of finding a player before he or she finds you. It is not cheap, " +
    								"you will need many valuable materials to start off. Read the next few pages for more information.", TextFormatting.GRAY);
    						return 1;
    					}
	    				else if (page == 3) {
    						StringFunctions.sendMessage(player, "Tracking Help Page (3/5) - Building", TextFormatting.DARK_GRAY, true);
    						StringFunctions.sendMessage(player, "To start building a solid tracker you will need a 'Diamond Block' for the middle, " +
    								"4 'Gold Blocks' for the end of each 'arm', and as much 'Obsidian' " + 
    								"as you can find for the length of the arms. Every obsidian block equals 25 blocks in the real world. Build your tracker at a smart position. Not too " +
    								"close to spawn, and not at coords that are easy to guess. You are not the only one that will try to make one.", TextFormatting.GRAY);
    						StringFunctions.sendMessage(player, "To see a 'layout' of a tracker do '/track help 3 layout'", TextFormatting.DARK_GRAY);
    						return 1;
    					}
	    				else if (page == 4) {
    						StringFunctions.sendMessage(player, "Tracking Help Page (4/5) - /track all", TextFormatting.DARK_GRAY, true);
    						StringFunctions.sendMessage(player, "Once you have made your own solid tracker, it's time to find those bases! Stand on the Diamond block in the middle of your " +
    								"tracker and do /track all. If there are players within the range of your tracker they will show up. You will need a player on atleast 2 of your arms to " +
    								"continue to page 5. If not, try to increase the length of your arms or wait until there are more/different players online.", TextFormatting.GRAY);
    						StringFunctions.sendMessage(player, "To proceed to page 5 do '/track help 5'", TextFormatting.DARK_GRAY);
    						return 1;
    					}
	    				else if (page == 5) {
    						StringFunctions.sendMessage(player, "Tracking Help Page (5/5) - /track [playerName]", TextFormatting.DARK_GRAY, true);
    						StringFunctions.sendMessage(player, "If you are lucky enough to find a player on 2 different arms, it's time to lower the size of your arms. Keep moving the Gold " +
    								"block down/up your arm to pinpoint the location of the player you want to track. If he doesn't show up anymore after you moved the Gold block, place the " +
    								"block higher up until you are at a 1 block difference. Now all that is left is to add the length of the arm from the respective coordinate on your " +
    								"Diamond block and you've found the location of an enemy within 25 blocks!", TextFormatting.GRAY);
    						return 1;
    					}
	    				
	    				StringFunctions.sendMessage(player, "Tracking Help Page (1/5)", TextFormatting.DARK_GRAY, true);
	    				StringFunctions.sendMessage(player, "For an introduction to tracking read page 2.", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For information on building a tracker read page 3.", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For explanation on '/track all' read page 4.", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For explanation on '/track [playerName]' read page 5.", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "To select a page: '/track help [page]'", TextFormatting.GRAY);
	    				return 1;
			        })))
	    			.then(Commands.literal("help")
	    			.then(Commands.argument("page", IntegerArgumentType.integer(3, 3))
	    			.then(Commands.literal("layout")
	    			.executes((command) -> {
	    				PlayerEntity player = command.getSource().getPlayerOrException();
	    				
						StringFunctions.sendMessage(player, "Tracking Help Page (3/5) - Building LAYOUT", TextFormatting.DARK_GRAY, true);
						StringFunctions.sendMessage(player, "In the picture below you can see multiple letters and dashes. The dashes are Obsidian blocks, D is a Diamond block, and G are Gold blocks.", TextFormatting.GRAY);
						StringFunctions.sendMessage(player, "                                G", TextFormatting.GRAY);
						StringFunctions.sendMessage(player, "                                |", TextFormatting.GRAY);
						StringFunctions.sendMessage(player, "                           G--D--G ", TextFormatting.GRAY);
						StringFunctions.sendMessage(player, "                                |", TextFormatting.GRAY);
						StringFunctions.sendMessage(player, "                                G", TextFormatting.GRAY);
						StringFunctions.sendMessage(player, "", TextFormatting.GRAY);   				
	    				return 1;
	    			}))))
	    			.then(Commands.literal("all")
	    			.executes((command) -> {
	    				PlayerEntity player = command.getSource().getPlayerOrException();
	    				
	    				World world = player.getCommandSenderWorld();
	    				BlockPos bpos = player.blockPosition().below().immutable();
	    				Block block = world.getBlockState(bpos).getBlock();
	    				if(block.equals(Blocks.DIAMOND_BLOCK)) {
	    					Tracking tracker = new Tracking(Main.instance);
	    					tracker.setLoc(bpos.getX(), bpos.getY(), bpos.getZ());
	    					tracker.Track(player, null);
	    					return 1;
	    				}
	    				if(block.equals(Blocks.OBSIDIAN)) {
	    					StringFunctions.sendMessage(player, "You cannot track all with this type of tracker", TextFormatting.GRAY);
	    					return 1;
	    				}

	    				StringFunctions.sendMessage(player, "You need to be on a solid tracker to '/track all'", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "Do '/track help' for more information.", TextFormatting.GRAY);
			    		return 1;
			        }))
	    			.then(Commands.argument("playerName", StringArgumentType.string())
	    			.executes((command) -> {
	    				PlayerEntity player = command.getSource().getPlayerOrException();
	    				String playername = StringArgumentType.getString(command, "playerName");
	    				
	    				PlayerEntity other = PlayerFunctions.matchPlayer(player, playername);
	    				if(other != null) {
	    					Tracking tracker = new Tracking(Main.instance);
	    					BlockPos bpos = player.blockPosition().below().immutable();
	    					tracker.setLoc(bpos.getX(), bpos.getY(), bpos.getZ());
	    					tracker.Track(player, other);
	    					return 1;
	    				}
	    				StringFunctions.sendMessage(player, "Could not find player '" + playername + "'.", TextFormatting.GRAY);
	    				return 1;
	    			}))
	    			.executes((command) -> {
	    				PlayerEntity player = command.getSource().getPlayerOrException();
	    				StringFunctions.sendMessage(player, "Use '/track [playerName]'  or '/track all'.", TextFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "Or do '/track help' for information.", TextFormatting.GRAY);
	    				return 1;
	    			})
	    		);
	   }
}