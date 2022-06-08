/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.19.0, mod version: 2.1.
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

package com.natamus.silencemobs.events;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.silencemobs.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void onEntityDamage(LivingAttackEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity source = e.getSource().getEntity();
		if (source instanceof Player == false) {
			return;
		}
		
		if (entity instanceof Player) {
			return;
		}
		
		Player player = (Player)source;
		ItemStack mainhand = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!mainhand.getHoverName().getString().equals(ChatFormatting.GOLD + "The Silence Stick")) {
			return;
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
			if (ConfigHandler.GENERAL.renameSilencedMobs.get()) {
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
			if (ConfigHandler.GENERAL.renameSilencedMobs.get()) {
				entity.setCustomName(Component.literal("Silenced " + entityname));
			}
			else {
				StringFunctions.sendMessage(player, "The " + entityname.toLowerCase() + " has been silenced.", ChatFormatting.DARK_GREEN);
			}
		}
		e.setCanceled(true);
	}
}
