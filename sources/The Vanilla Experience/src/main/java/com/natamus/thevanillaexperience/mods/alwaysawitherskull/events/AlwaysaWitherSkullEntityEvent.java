/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.alwaysawitherskull.events;

import java.util.Collection;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AlwaysaWitherSkullEntityEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof WitherSkeleton == false) {
			return;
		}
		
		Boolean foundskull = false;
		
		Collection<ItemEntity> drops = e.getDrops();
		for (ItemEntity eitem : drops) {
			if (eitem.getItem().getItem().equals(Items.WITHER_SKELETON_SKULL)) {
				foundskull = true;
				break;
			}
		}
		
		if (!foundskull) {
			ItemEntity newskull = new ItemEntity(entity.level, entity.getX(), entity.getY()+1, entity.getZ(), new ItemStack(Items.WITHER_SKELETON_SKULL, 1));
			drops.add(newskull);
		}
	}
}
