/*
 * This is the latest source code of Kelp Fertilizer.
 * Minecraft version: 1.17.1, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Kelp Fertilizer ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.kelpfertilizer.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class KelpEvent {
	@SubscribeEvent
	public void onKelpUse(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (!itemstack.getItem().equals(Items.KELP)) {
			return;
		}
		
		Player player = e.getPlayer();
		BlockPos cpos = e.getPos();
		if (BoneMealItem.applyBonemeal(itemstack, world, cpos, player)) {
			world.levelEvent(2005, cpos, 0);
			
			if (player.isCreative()) {
				itemstack.grow(1);
			}
		}
	}
}