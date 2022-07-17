/*
 * This is the latest source code of Milk All The Mobs.
 * Minecraft version: 1.19.0, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Milk All The Mobs ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.milkallthemobs.events;

import com.natamus.collective.functions.EntityFunctions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MilkEvent {
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.BUCKET)) {
			Entity target = e.getTarget();
			if (EntityFunctions.isMilkable(target)) {
				Player player = e.getEntity();
				
				player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
				itemstack.shrink(1);
				
				if (itemstack.isEmpty()) {
					player.setItemInHand(e.getHand(), new ItemStack(Items.MILK_BUCKET));
				}
				else if (!player.getInventory().add(new ItemStack(Items.MILK_BUCKET))) {
					player.drop(new ItemStack(Items.MILK_BUCKET), false);
				}
				e.setCancellationResult(InteractionResult.SUCCESS);
			}
		}
	}
}
