/*
 * This is the latest source code of Humbling Bundle.
 * Minecraft version: 1.19.3, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.humblingbundle.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof Player) {
			return;
		}
		
		List<ItemStack> equipment = new ArrayList<ItemStack>();
		Iterator<ItemStack> equipmentiterator = entity.getAllSlots().iterator();
		while (equipmentiterator.hasNext()) {
			ItemStack next = equipmentiterator.next();
			equipment.add(next);
		}
		
		Iterator<ItemEntity> iterator = e.getDrops().iterator();
		while (iterator.hasNext()) {
			ItemEntity drop = iterator.next();
			ItemStack item = drop.getItem();
			if (equipment.contains(item)) {
				continue;
			}
			int count = item.getCount()*2;
			item.setCount(count);
			drop.setItem(item);
		}
	}
}