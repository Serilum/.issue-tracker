/*
 * This is the latest source code of Entity Information.
 * Minecraft version: 1.19.1, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Entity Information ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
