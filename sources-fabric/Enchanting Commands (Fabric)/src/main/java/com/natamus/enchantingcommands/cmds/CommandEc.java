/*
 * This is the latest source code of Enchanting Commands.
 * Minecraft version: 1.17.x, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Enchanting Commands ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.enchantingcommands.cmds;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.enchantingcommands.config.ConfigHandler;
import com.natamus.enchantingcommands.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class CommandEc {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal(ConfigHandler.enchantCommandString.getValue())
			.requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				sendUsage(command.getSource());
				return 1;
			})
			.then(Commands.literal("list")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();

				String joined = String.join(", ", Util.enchantments());
				StringFunctions.sendMessage(source, "--- Enchanting Commands List ---", ChatFormatting.DARK_GREEN, true);
				StringFunctions.sendMessage(source, " " + joined, ChatFormatting.DARK_GREEN);
				return 1;
			}))
			.then(Commands.literal("enchant")
			.then(Commands.argument("enchantment", StringArgumentType.word())
			.then(Commands.argument("level", IntegerArgumentType.integer(0, 127))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Entity entity = source.getEntity();
				if (entity instanceof ServerPlayer == false) {
					StringFunctions.sendMessage(source, "This command can only be executed as a player.", ChatFormatting.RED);
					return 1;
				}
				
				Player player = (ServerPlayer)entity;
				ItemStack held = player.getMainHandItem();
				
				String enchantment = StringArgumentType.getString(command, "enchantment");
				Integer level = IntegerArgumentType.getInteger(command, "level");
				
				List<String> enchantments = Util.enchantments();
				if (!player.hasItemInSlot(EquipmentSlot.MAINHAND)) {
					StringFunctions.sendMessage(player, "You do not have an enchantable item in your main hand.", ChatFormatting.RED);
					return 0;
				}
				
				if (!enchantments.contains(enchantment.toLowerCase())) {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantment + "' does not exist. See '/" + ConfigHandler.enchantCommandString.getValue() + " list' for the available enchantments.", ChatFormatting.RED);
					return 0;
				}
				
				Enchantment enchant = Registry.ENCHANTMENT.byId(Util.getEnchantmentID(enchantment.toLowerCase()));
				
				ItemStack temp = new ItemStack(Item.byId(1));
				temp.enchant(enchant, level);
				String estringtemp = temp.getEnchantmentTags().get(0).toString().split("id:")[1];
				
				Boolean removed = false;
				for (Tag nbt : held.getEnchantmentTags()) {
					if (estringtemp.equals(nbt.toString().split("id:")[1])) {
						held.getEnchantmentTags().remove(nbt);
						removed = true;
						break;
					}
				}
				
				if (level != 0) {
					held.enchant(enchant, level);
					StringFunctions.sendMessage(player, "The enchantment '" + enchantment.toLowerCase() + "' has been added to the item with a level of " + level + ".", ChatFormatting.DARK_GREEN);
				}
				else if (removed) {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantment.toLowerCase() + "' has been removed from the item.", ChatFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantment.toLowerCase() + "' does not exist on the item.", ChatFormatting.RED);
				}
				return 1;
			}))))
		);
    }
    
	public static void sendUsage(CommandSourceStack source) {
		StringFunctions.sendMessage(source, "--- Enchanting Commands Usage ---", ChatFormatting.DARK_GREEN, true);
		StringFunctions.sendMessage(source, " /" + ConfigHandler.enchantCommandString.getValue() + " list", ChatFormatting.DARK_GREEN);
		StringFunctions.sendMessage(source, " /" + ConfigHandler.enchantCommandString.getValue() + " enchant <enchant> <lvl>", ChatFormatting.DARK_GREEN);
		return;
	}

	public static void sendUsage(Player player) {
		StringFunctions.sendMessage(player, "--- Enchanting Commands Usage ---", ChatFormatting.DARK_GREEN, true);
		StringFunctions.sendMessage(player, " /" + ConfigHandler.enchantCommandString.getValue() + " list", ChatFormatting.DARK_GREEN);
		StringFunctions.sendMessage(player, " /" + ConfigHandler.enchantCommandString.getValue() + " enchant <enchant> <lvl>", ChatFormatting.DARK_GREEN);
		return;
	}
}