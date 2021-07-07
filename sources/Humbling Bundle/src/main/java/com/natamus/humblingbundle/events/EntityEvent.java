/*
 * This is the latest source code of Humbling Bundle.
 * Minecraft version: 1.16.5, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Humbling Bundle ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.humblingbundle.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof PlayerEntity) {
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