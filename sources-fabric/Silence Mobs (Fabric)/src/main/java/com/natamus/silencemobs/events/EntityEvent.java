/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.silencemobs.events;

import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.silencemobs.config.ConfigHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityEvent {
	public static boolean onEntityDamage(Level world, Entity entity, DamageSource damageSource, float damageAmount) {
		if (world.isClientSide) {
			return true;
		}
		
		Entity source = damageSource.getEntity();
		if (source instanceof Player == false) {
			return true;
		}
		
		if (entity instanceof Player) {
			return true;
		}
		
		Player player = (Player)source;
		ItemStack mainhand = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!mainhand.getHoverName().getString().equals(ChatFormatting.GOLD + "The Silence Stick")) {
			return true;
		}
		
		String defaultname = EntityFunctions.getEntityString(entity).split("\\[")[0].replace("Entity", "");
		String entityname = "entity";
		
		
		if (entity.hasCustomName()) {
			entityname = entity.getCustomName().getString() + " ";
		}
		else if (defaultname != "") {
			entityname = defaultname;
		}
			
		if (entity.isSilent()) {
			entity.setSilent(false);
			if (ConfigHandler.renameSilencedMobs.getValue()) {
				if (entityname.substring(entityname.length() - 1).equals(" ") && entityname.toLowerCase().contains("silenced")) {
					entityname = entityname.replace("Silenced ", "").trim();
					if (entityname.toLowerCase().equals(defaultname.toLowerCase())) {
						entity.setCustomName(null);
					}
					else {
						entity.setCustomName(Component.literal(entityname.trim()));
					}
				}
				else {
					entity.setCustomName(null);
				}
			}
			else {
				StringFunctions.sendMessage(player, "The " + entityname.toLowerCase() + " has been unsilenced.", ChatFormatting.DARK_GREEN);
			}
		}
		else {
			entity.setSilent(true);
			if (ConfigHandler.renameSilencedMobs.getValue()) {
				entity.setCustomName(Component.literal("Silenced " + entityname));
			}
			else {
				StringFunctions.sendMessage(player, "The " + entityname.toLowerCase() + " has been silenced.", ChatFormatting.DARK_GREEN);
			}
		}
		
		return false;
	}
}
