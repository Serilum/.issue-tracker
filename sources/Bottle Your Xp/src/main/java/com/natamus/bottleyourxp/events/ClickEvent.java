/*
 * This is the latest source code of Bottle Your Xp.
 * Minecraft version: 1.18.0, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Bottle Your Xp ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.GLASS_BOTTLE)) {
			Player player = e.getPlayer();
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
