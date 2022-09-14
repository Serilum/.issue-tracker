/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.19.2, mod version: 2.3.
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

package com.natamus.silencemobs.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.silencemobs.config.ConfigHandler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class CommandSt {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("st")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player && (!ConfigHandler.GENERAL.onlyAllowCommandWhenCheatsEnabled.get() || iCommandSender.hasPermission(2)))
			.executes((command) -> {
				processSilencestick(command);
				return 1;
			})
		);
    	dispatcher.register(Commands.literal("silencestick")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player && (!ConfigHandler.GENERAL.onlyAllowCommandWhenCheatsEnabled.get() || iCommandSender.hasPermission(2)))
			.executes((command) -> {
				processSilencestick(command);
				return 1;
			})
		);
    }
    
    public static void processSilencestick(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
    	CommandSourceStack source = command.getSource();
    	Player player = (Player)source.getPlayerOrException();
		
		if (ConfigHandler.GENERAL.mustHoldStick.get()) {
			ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
			if (!held.sameItem(new ItemStack(Items.STICK, 1))) {
				StringFunctions.sendMessage(player, "You must hold a stick in your main hand to transform it into a silence-stick.", ChatFormatting.DARK_RED);
				return;
			}
			player.getMainHandItem().shrink(1);
		}
		
		ItemStack silencestick = new ItemStack(Items.STICK, 1);
		silencestick.setHoverName(Component.literal(ChatFormatting.GOLD + "The Silence Stick"));
		player.addItem(silencestick);
		StringFunctions.sendMessage(player, "You have been given The Silence Stick! Handle with care.", ChatFormatting.DARK_GREEN);
		return;			
    }
}