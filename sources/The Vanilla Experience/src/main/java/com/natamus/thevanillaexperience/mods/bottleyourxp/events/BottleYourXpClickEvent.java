/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.bottleyourxp.events;

import com.natamus.thevanillaexperience.mods.bottleyourxp.config.BottleYourXpConfigHandler;
import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.ExperienceFunctions;
import com.natamus.collective.functions.ItemFunctions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BottleYourXpClickEvent {
	@SubscribeEvent
	public void onClickBottle(PlayerInteractEvent.RightClickItem e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.GLASS_BOTTLE)) {
			PlayerEntity player = e.getPlayer();
			if (player.isShiftKeyDown()) {
				if (ExperienceFunctions.canConsumeXp(player, BottleYourXpConfigHandler.GENERAL.rawXpConsumedOnCreation.get())) {
					if (BottleYourXpConfigHandler.GENERAL.damageOnCreation.get() && !player.isCreative()) {
						int damage = BottleYourXpConfigHandler.GENERAL.halfHeartDamageAmount.get();
						if (!EntityFunctions.doesEntitySurviveThisDamage(player, damage)) {
							return;
						}
					}
					
					ExperienceFunctions.addPlayerXP(player, -BottleYourXpConfigHandler.GENERAL.rawXpConsumedOnCreation.get());
					ItemFunctions.shrinkGiveOrDropItemStack(player, e.getHand(), itemstack, new ItemStack(Items.EXPERIENCE_BOTTLE, 1));
				}
			}
		}
	}
}
