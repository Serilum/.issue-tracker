/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.16.5, mod version: 1.9.
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

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void onEntityDamage(LivingAttackEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity source = e.getSource().getEntity();
		if (source instanceof PlayerEntity == false) {
			return;
		}
		
		if (entity instanceof PlayerEntity) {
			return;
		}
		
		PlayerEntity player = (PlayerEntity)source;
		ItemStack mainhand = player.getItemInHand(Hand.MAIN_HAND);
		if (!mainhand.getHoverName().getString().equals(TextFormatting.GOLD + "The Silence Stick")) {
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
						entity.setCustomName(new StringTextComponent(entityname.trim()));
					}
				}
				else {
					entity.setCustomName(null);
				}
			}
			else {
				StringFunctions.sendMessage(player, "The " + entityname.toLowerCase() + " has been unsilenced.", TextFormatting.DARK_GREEN);
			}
		}
		else {
			entity.setSilent(true);
			if (ConfigHandler.GENERAL.renameSilencedMobs.get()) {
				entity.setCustomName(new StringTextComponent("Silenced " + entityname));
			}
			else {
				StringFunctions.sendMessage(player, "The " + entityname.toLowerCase() + " has been silenced.", TextFormatting.DARK_GREEN);
			}
		}
		e.setCanceled(true);
	}
}
