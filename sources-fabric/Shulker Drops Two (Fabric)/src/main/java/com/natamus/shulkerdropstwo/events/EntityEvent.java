/*
 * This is the latest source code of Shulker Drops Two.
 * Minecraft version: 1.19.x, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Shulker Drops Two ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.shulkerdropstwo.events;

import java.util.ArrayList;
import java.util.List;

import com.natamus.collective_fabric.functions.TaskFunctions;
import com.natamus.shulkerdropstwo.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityEvent {
	public static void mobItemDrop(Level world, Entity entity, DamageSource damageSource) {
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof Shulker == false) {
			return;
		}
		
		BlockPos epos = entity.blockPosition();
		
		TaskFunctions.enqueueTask(world, new Runnable() {
			@Override
			public void run() {
				List<ItemEntity> shellies = new ArrayList<ItemEntity>();
				
				int r = 1;
				for (Entity ea : world.getEntities(null, new AABB(epos.getX()-r, epos.getY()-r, epos.getZ()-r, epos.getX()+r, epos.getY()+r, epos.getZ()+r))) {
					if (ea instanceof ItemEntity) {
						ItemEntity ie = (ItemEntity)ea;
						if (ie.getItem().getItem().equals(Items.SHULKER_SHELL)) {
							if (ie.blockPosition().equals(epos)) {
								shellies.add(ie);
							}
						}
					}
				}
				
				if (shellies.size() > 0) {
					for (ItemEntity shellentity : shellies) {
						ItemStack item = shellentity.getItem();

						item.setCount(ConfigHandler.shulkerDropAmount.getValue());
						shellentity.setItem(item);
					}
				}
				else if (ConfigHandler.alwaysDropShells.getValue()) {
					ItemEntity newshell = new ItemEntity(world, epos.getX(), epos.getY(), epos.getZ(), new ItemStack(Items.SHULKER_SHELL, ConfigHandler.shulkerDropAmount.getValue()));

					world.addFreshEntity(newshell);
				}
			}
		}, 0);
	}
}
