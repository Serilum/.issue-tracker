/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.19.1, mod version: 2.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Silence Mobs ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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