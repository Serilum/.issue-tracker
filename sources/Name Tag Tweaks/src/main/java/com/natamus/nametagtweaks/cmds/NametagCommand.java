/*
 * This is the latest source code of Name Tag Tweaks.
 * Minecraft version: 1.17.1, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Name Tag Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.nametagtweaks.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.nametagtweaks.config.ConfigHandler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;

public class NametagCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("nametag")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof ServerPlayer)
			.executes((command) -> {
				StringFunctions.sendMessage(command.getSource(), "Usage: '/nametag <name>' while holding a name tag.", ChatFormatting.DARK_GREEN);
				return 1;
			})
			.then(Commands.argument("name", StringArgumentType.word())
			.executes((command) -> {
				Player player = command.getSource().getPlayerOrException();
				
				ItemStack nametagstack;
				if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.NAME_TAG)) {
					nametagstack = player.getItemInHand(InteractionHand.MAIN_HAND);
				}
				else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem().equals(Items.NAME_TAG)) {
					nametagstack = player.getItemInHand(InteractionHand.OFF_HAND);
				}
				else {
					StringFunctions.sendMessage(player, "Usage: '/nametag <name>' while holding a name tag.", ChatFormatting.RED);
					return 1;
				}
				
				String name = StringArgumentType.getString(command, "name");
				if (ConfigHandler.GENERAL.nameTagCommandReplaceUnderscoresWithSpaces.get()) {
					name = name.replace("_", " ");
				}
				
				nametagstack.setHoverName(new TextComponent(name));
				nametagstack.setRepairCost(0);
				StringFunctions.sendMessage(player, "Set name value to '" + name + "'.", ChatFormatting.DARK_GREEN);
				return 1;
			}))
		);
    }
}
