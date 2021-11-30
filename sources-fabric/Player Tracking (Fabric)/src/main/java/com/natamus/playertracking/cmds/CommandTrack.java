/*
 * This is the latest source code of Player Tracking.
 * Minecraft version: 1.18.x, mod version: 1.6.
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
import com.natamus.collective_fabric.functions.PlayerFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.playertracking.Main;
import com.natamus.playertracking.util.Tracking;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

public class CommandTrack {
	   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
	    	dispatcher.register(Commands.literal("track")
	    			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
	    			.then(Commands.literal("help")
	    			.executes((command) -> {
	    				Player player = command.getSource().getPlayerOrException();
	    				
	    				StringFunctions.sendMessage(player, "Tracking Help Page (1/5)", ChatFormatting.DARK_GRAY, true);
	    				StringFunctions.sendMessage(player, "For an introduction to tracking read page 2.", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For information on building a tracker read page 3.", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For explanation on '/track all' read page 4.", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For explanation on '/track [playerName]' read page 5.", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "To select a page: '/track help [page]'", ChatFormatting.GRAY);
	    				return 1;
	    			}))
	    			.then(Commands.literal("help")
	    			.then(Commands.argument("page", IntegerArgumentType.integer(1, 5))
	    			.executes((command) -> {
	    				Player player = command.getSource().getPlayerOrException();
	    				
	    				Integer page = IntegerArgumentType.getInteger(command, "page");
	    				
	    				if (page == 2) {
    						StringFunctions.sendMessage(player, "Tracking Help Page (2/5) - Introduction", ChatFormatting.DARK_GRAY, true);
    						StringFunctions.sendMessage(player, "Tracking is a feature to encourage raiding on a server. Without this it would be near-impossible to find " +
    								"enemy bases legit, and would give hackers an unfair advantage. Tracking is the art of finding a player before he or she finds you. It is not cheap, " +
    								"you will need many valuable materials to start off. Read the next few pages for more information.", ChatFormatting.GRAY);
    						return 1;
    					}
	    				else if (page == 3) {
    						StringFunctions.sendMessage(player, "Tracking Help Page (3/5) - Building", ChatFormatting.DARK_GRAY, true);
    						StringFunctions.sendMessage(player, "To start building a solid tracker you will need a 'Diamond Block' for the middle, " +
    								"4 'Gold Blocks' for the end of each 'arm', and as much 'Obsidian' " + 
    								"as you can find for the length of the arms. Every obsidian block equals 25 blocks in the real world. Build your tracker at a smart position. Not too " +
    								"close to spawn, and not at coords that are easy to guess. You are not the only one that will try to make one.", ChatFormatting.GRAY);
    						StringFunctions.sendMessage(player, "To see a 'layout' of a tracker do '/track help 3 layout'", ChatFormatting.DARK_GRAY);
    						return 1;
    					}
	    				else if (page == 4) {
    						StringFunctions.sendMessage(player, "Tracking Help Page (4/5) - /track all", ChatFormatting.DARK_GRAY, true);
    						StringFunctions.sendMessage(player, "Once you have made your own solid tracker, it's time to find those bases! Stand on the Diamond block in the middle of your " +
    								"tracker and do /track all. If there are players within the range of your tracker they will show up. You will need a player on atleast 2 of your arms to " +
    								"continue to page 5. If not, try to increase the length of your arms or wait until there are more/different players online.", ChatFormatting.GRAY);
    						StringFunctions.sendMessage(player, "To proceed to page 5 do '/track help 5'", ChatFormatting.DARK_GRAY);
    						return 1;
    					}
	    				else if (page == 5) {
    						StringFunctions.sendMessage(player, "Tracking Help Page (5/5) - /track [playerName]", ChatFormatting.DARK_GRAY, true);
    						StringFunctions.sendMessage(player, "If you are lucky enough to find a player on 2 different arms, it's time to lower the size of your arms. Keep moving the Gold " +
    								"block down/up your arm to pinpoint the location of the player you want to track. If he doesn't show up anymore after you moved the Gold block, place the " +
    								"block higher up until you are at a 1 block difference. Now all that is left is to add the length of the arm from the respective coordinate on your " +
    								"Diamond block and you've found the location of an enemy within 25 blocks!", ChatFormatting.GRAY);
    						return 1;
    					}
	    				
	    				StringFunctions.sendMessage(player, "Tracking Help Page (1/5)", ChatFormatting.DARK_GRAY, true);
	    				StringFunctions.sendMessage(player, "For an introduction to tracking read page 2.", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For information on building a tracker read page 3.", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For explanation on '/track all' read page 4.", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "For explanation on '/track [playerName]' read page 5.", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "To select a page: '/track help [page]'", ChatFormatting.GRAY);
	    				return 1;
			        })))
	    			.then(Commands.literal("help")
	    			.then(Commands.argument("page", IntegerArgumentType.integer(3, 3))
	    			.then(Commands.literal("layout")
	    			.executes((command) -> {
	    				Player player = command.getSource().getPlayerOrException();
	    				
						StringFunctions.sendMessage(player, "Tracking Help Page (3/5) - Building LAYOUT", ChatFormatting.DARK_GRAY, true);
						StringFunctions.sendMessage(player, "In the picture below you can see multiple letters and dashes. The dashes are Obsidian blocks, D is a Diamond block, and G are Gold blocks.", ChatFormatting.GRAY);
						StringFunctions.sendMessage(player, "                                G", ChatFormatting.GRAY);
						StringFunctions.sendMessage(player, "                                |", ChatFormatting.GRAY);
						StringFunctions.sendMessage(player, "                           G--D--G ", ChatFormatting.GRAY);
						StringFunctions.sendMessage(player, "                                |", ChatFormatting.GRAY);
						StringFunctions.sendMessage(player, "                                G", ChatFormatting.GRAY);
						StringFunctions.sendMessage(player, "", ChatFormatting.GRAY);   				
	    				return 1;
	    			}))))
	    			.then(Commands.literal("all")
	    			.executes((command) -> {
	    				Player player = command.getSource().getPlayerOrException();
	    				
	    				Level world = player.getCommandSenderWorld();
	    				BlockPos bpos = player.blockPosition().below().immutable();
	    				Block block = world.getBlockState(bpos).getBlock();
	    				if(block.equals(Blocks.DIAMOND_BLOCK)) {
	    					Tracking tracker = new Tracking(Main.instance);
	    					tracker.setLoc(bpos.getX(), bpos.getY(), bpos.getZ());
	    					tracker.Track(player, null);
	    					return 1;
	    				}
	    				if(block.equals(Blocks.OBSIDIAN)) {
	    					StringFunctions.sendMessage(player, "You cannot track all with this type of tracker", ChatFormatting.GRAY);
	    					return 1;
	    				}

	    				StringFunctions.sendMessage(player, "You need to be on a solid tracker to '/track all'", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "Do '/track help' for more information.", ChatFormatting.GRAY);
			    		return 1;
			        }))
	    			.then(Commands.argument("playerName", StringArgumentType.string())
	    			.executes((command) -> {
	    				Player player = command.getSource().getPlayerOrException();
	    				String playername = StringArgumentType.getString(command, "playerName");
	    				
	    				Player other = PlayerFunctions.matchPlayer(player, playername);
	    				if(other != null) {
	    					Tracking tracker = new Tracking(Main.instance);
	    					BlockPos bpos = player.blockPosition().below().immutable();
	    					tracker.setLoc(bpos.getX(), bpos.getY(), bpos.getZ());
	    					tracker.Track(player, other);
	    					return 1;
	    				}
	    				StringFunctions.sendMessage(player, "Could not find player '" + playername + "'.", ChatFormatting.GRAY);
	    				return 1;
	    			}))
	    			.executes((command) -> {
	    				Player player = command.getSource().getPlayerOrException();
	    				StringFunctions.sendMessage(player, "Use '/track [playerName]'  or '/track all'.", ChatFormatting.GRAY);
	    				StringFunctions.sendMessage(player, "Or do '/track help' for information.", ChatFormatting.GRAY);
	    				return 1;
	    			})
	    		);
	   }
}