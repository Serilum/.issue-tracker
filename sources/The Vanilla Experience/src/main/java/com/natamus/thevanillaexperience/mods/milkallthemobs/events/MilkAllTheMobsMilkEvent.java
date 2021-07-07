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

package com.natamus.thevanillaexperience.mods.milkallthemobs.events;

import com.natamus.collective.functions.EntityFunctions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MilkAllTheMobsMilkEvent {
	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.BUCKET)) {
			Entity target = e.getTarget();
			if (EntityFunctions.isMilkable(target)) {
				PlayerEntity player = e.getPlayer();
				
				player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
				itemstack.shrink(1);
				
				if (itemstack.isEmpty()) {
					player.setItemInHand(e.getHand(), new ItemStack(Items.MILK_BUCKET));
				}
				else if (!player.inventory.add(new ItemStack(Items.MILK_BUCKET))) {
					player.drop(new ItemStack(Items.MILK_BUCKET), false);
				}
				e.setCancellationResult(ActionResultType.SUCCESS);
			}
		}
	}
}
