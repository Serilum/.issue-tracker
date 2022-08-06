/*
 * This is the latest source code of Milk All The Mobs.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
