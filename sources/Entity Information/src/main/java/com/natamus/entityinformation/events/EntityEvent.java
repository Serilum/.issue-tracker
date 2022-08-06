/*
 * This is the latest source code of Entity Information.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.entityinformation.events;

import com.natamus.collective.functions.StringFunctions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void onEntityDamage(LivingAttackEvent e) {
		Entity source = e.getSource().getEntity();
		if (source == null) {
			return;
		}
		
		Level world = source.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (source instanceof Player == false) {
			return;
		}
		
		Entity entity = e.getEntity();
		Player player = (Player)source;

		ItemStack mainhand = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!mainhand.getItem().equals(Items.STICK)) {
			return;
		}
		
		if (!mainhand.getHoverName().getString().equals(ChatFormatting.BLUE + "The Information Stick")) {
			return;
		}

		String name = "Name: " + entity.getName().getString();
		String entityName = "Entity" + name;
		try {
			entityName = "EntityName: " + entity.toString().split("\\[")[0];
		}
		catch (Exception ex) {}
		String entityId = "EntityId: " + Integer.toString(entity.getId());
		String UUID = "UUID: " + entity.getUUID().toString();
		String position = "Position: " + entity.blockPosition().toString().replace("BlockPos{", "").replace("}", "");
		String isSilent = "isSilent: " + String.valueOf(entity.isSilent());
		String ticksExisted = "ticksExisted: " + Integer.toString(entity.tickCount);

		StringFunctions.sendMessage(player, "---- Entity Information:", ChatFormatting.BLUE, true);
		StringFunctions.sendMessage(player, name, ChatFormatting.BLUE);
		StringFunctions.sendMessage(player, entityName, ChatFormatting.BLUE);
		StringFunctions.sendMessage(player, entityId, ChatFormatting.BLUE);
		StringFunctions.sendMessage(player, UUID, ChatFormatting.BLUE);
		StringFunctions.sendMessage(player, position, ChatFormatting.BLUE);
		StringFunctions.sendMessage(player, isSilent, ChatFormatting.BLUE);
		StringFunctions.sendMessage(player, ticksExisted, ChatFormatting.BLUE);
		e.setCanceled(true);
	}
}
