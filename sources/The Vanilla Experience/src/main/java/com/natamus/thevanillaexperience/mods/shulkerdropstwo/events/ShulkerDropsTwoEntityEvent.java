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

package com.natamus.thevanillaexperience.mods.shulkerdropstwo.events;

import java.util.Collection;

import com.natamus.thevanillaexperience.mods.shulkerdropstwo.config.ShulkerDropsTwoConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ShulkerDropsTwoEntityEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		if (entity instanceof ShulkerEntity == false) {
			return;
		}
		
		Collection<ItemEntity> drops = e.getDrops();
		if (drops.size() < 1 && !ShulkerDropsTwoConfigHandler.GENERAL.alwaysDropShells.get()) {
			return;
		}
		
		BlockPos pos = entity.blockPosition();
		
		ItemEntity shells = new ItemEntity(world, pos.getX(), pos.getY()+1, pos.getZ(), new ItemStack(Items.SHULKER_SHELL, ShulkerDropsTwoConfigHandler.GENERAL.shulkerDropAmount.get()));
		
		e.getDrops().clear();
		e.getDrops().add(shells);
	}
}
