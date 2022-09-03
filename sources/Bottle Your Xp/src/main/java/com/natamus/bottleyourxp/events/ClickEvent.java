/*
 * This is the latest source code of Bottle Your Xp.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.bottleyourxp.events;

import com.natamus.bottleyourxp.config.ConfigHandler;
import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.ExperienceFunctions;
import com.natamus.collective.functions.ItemFunctions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ClickEvent {
	@SubscribeEvent
	public void onClickBottle(PlayerInteractEvent.RightClickItem e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.GLASS_BOTTLE)) {
			Player player = e.getEntity();
			if (player.isShiftKeyDown()) {
				if (ExperienceFunctions.canConsumeXp(player, ConfigHandler.GENERAL.rawXpConsumedOnCreation.get())) {
					if (ConfigHandler.GENERAL.damageOnCreation.get() && !player.isCreative()) {
						int damage = ConfigHandler.GENERAL.halfHeartDamageAmount.get();
						if (!EntityFunctions.doesEntitySurviveThisDamage(player, damage)) {
							return;
						}
					}
					
					ExperienceFunctions.addPlayerXP(player, -ConfigHandler.GENERAL.rawXpConsumedOnCreation.get());
					ItemFunctions.shrinkGiveOrDropItemStack(player, e.getHand(), itemstack, new ItemStack(Items.EXPERIENCE_BOTTLE, 1));
				}
			}
		}
	}
}
