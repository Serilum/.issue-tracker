/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.enchantingcommands.cmds;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.enchantingcommands.config.EnchantingCommandsConfigHandler;
import com.natamus.thevanillaexperience.mods.enchantingcommands.util.EnchantingCommandsUtil;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TextFormatting;

public class CommandEc {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal(EnchantingCommandsConfigHandler.GENERAL.enchantCommandString.get())
			.requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				sendUsage(command.getSource());
				return 1;
			})
			.then(Commands.literal("list")
			.executes((command) -> {
				CommandSource source = command.getSource();

				String joined = String.join(", ", EnchantingCommandsUtil.enchantments());
				StringFunctions.sendMessage(source, "--- Enchanting Commands List ---", TextFormatting.DARK_GREEN, true);
				StringFunctions.sendMessage(source, " " + joined, TextFormatting.DARK_GREEN);
				return 1;
			}))
			.then(Commands.literal("enchant")
			.then(Commands.argument("enchantment", StringArgumentType.word())
			.then(Commands.argument("level", IntegerArgumentType.integer(0, 127))
			.executes((command) -> {
				CommandSource source = command.getSource();
				Entity entity = source.getEntity();
				if (entity instanceof ServerPlayerEntity == false) {
					StringFunctions.sendMessage(source, "This command can only be executed as a player.", TextFormatting.RED);
					return 1;
				}
				
				PlayerEntity player = (ServerPlayerEntity)entity;
				ItemStack held = player.getMainHandItem();
				
				String enchantment = StringArgumentType.getString(command, "enchantment");
				Integer level = IntegerArgumentType.getInteger(command, "level");
				
				List<String> enchantments = EnchantingCommandsUtil.enchantments();
				if (!player.hasItemInSlot(EquipmentSlotType.MAINHAND)) {
					StringFunctions.sendMessage(player, "You do not have an enchantable item in your main hand.", TextFormatting.RED);
					return 0;
				}
				
				if (!enchantments.contains(enchantment.toLowerCase())) {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantment + "' does not exist. See '/" + EnchantingCommandsConfigHandler.GENERAL.enchantCommandString.get() + " list' for the available enchantments.", TextFormatting.RED);
					return 0;
				}
				
				@SuppressWarnings("deprecation")
				Enchantment enchant = Registry.ENCHANTMENT.byId(EnchantingCommandsUtil.getEnchantmentID(enchantment.toLowerCase()));
				
				ItemStack temp = new ItemStack(Item.byId(1));
				temp.enchant(enchant, level);
				String estringtemp = temp.getEnchantmentTags().get(0).toString().split("id:")[1];
				
				Boolean removed = false;
				for (INBT nbt : held.getEnchantmentTags()) {
					if (estringtemp.equals(nbt.toString().split("id:")[1])) {
						held.getEnchantmentTags().remove(nbt);
						removed = true;
						break;
					}
				}
				
				if (level != 0) {
					held.enchant(enchant, level);
					StringFunctions.sendMessage(player, "The enchantment '" + enchantment.toLowerCase() + "' has been added to the item with a level of " + level + ".", TextFormatting.DARK_GREEN);
				}
				else if (removed) {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantment.toLowerCase() + "' has been removed from the item.", TextFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantment.toLowerCase() + "' does not exist on the item.", TextFormatting.RED);
				}
				return 1;
			}))))
		);
    }
    
	public static void sendUsage(CommandSource source) {
		StringFunctions.sendMessage(source, "--- Enchanting Commands Usage ---", TextFormatting.DARK_GREEN, true);
		StringFunctions.sendMessage(source, " /" + EnchantingCommandsConfigHandler.GENERAL.enchantCommandString.get() + " list", TextFormatting.DARK_GREEN);
		StringFunctions.sendMessage(source, " /" + EnchantingCommandsConfigHandler.GENERAL.enchantCommandString.get() + " enchant <enchant> <lvl>", TextFormatting.DARK_GREEN);
		return;
	}

	public static void sendUsage(PlayerEntity player) {
		StringFunctions.sendMessage(player, "--- Enchanting Commands Usage ---", TextFormatting.DARK_GREEN, true);
		StringFunctions.sendMessage(player, " /" + EnchantingCommandsConfigHandler.GENERAL.enchantCommandString.get() + " list", TextFormatting.DARK_GREEN);
		StringFunctions.sendMessage(player, " /" + EnchantingCommandsConfigHandler.GENERAL.enchantCommandString.get() + " enchant <enchant> <lvl>", TextFormatting.DARK_GREEN);
		return;
	}
}