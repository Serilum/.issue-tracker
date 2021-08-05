/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.alllootdrops.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.natamus.thevanillaexperience.mods.alllootdrops.config.AllLootDropsConfigHandler;
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AllLootDropsEntityEvent {
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (GlobalVariables.entitydrops != null) {
			return;
		}
		
		ItemFunctions.generateEntityDropsFromLootTable(world);
	}
	
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof PlayerEntity || entity instanceof LivingEntity == false) {
			return;
		}
		
		if (GlobalVariables.entitydrops == null) {
			System.out.println("[All Loot Drops] Error: Unable to find generated loot drops. Attempting to generate them now.");
			ItemFunctions.generateEntityDropsFromLootTable(world);
			if (GlobalVariables.entitydrops == null) {
				System.out.println("[All Loot Drops] Error: Still unable to generate loot drops. Please submit a bug report at 'https://github.com/ricksouth/serilum-mc-mods/labels/Mod:%20All%20Loot%20Drops'.");
				return;
			}
		}

		LivingEntity le = (LivingEntity)entity;
		EntityType<?> type = le.getType();
		
		List<Item> alldrops = GlobalVariables.entitydrops.get(type);
		if (alldrops == null) {
			return;
		}
		
		int amount = AllLootDropsConfigHandler.GENERAL.lootQuantity.get();
		if (AllLootDropsConfigHandler.GENERAL.lootingEnchantAffectsQuantity.get()) {
			int lootinglevel = e.getLootingLevel();
			double increasechance = AllLootDropsConfigHandler.GENERAL.lootingEnchantExtraQuantityChance.get();
			for (int n = 0; n < lootinglevel; n++) {
				double num = GlobalVariables.random.nextDouble();
				if (num <= increasechance) {
					amount+=1;
				}
			}
		}
		
		Vector3d evec = entity.position();
		
		Collection<ItemEntity> drops = e.getDrops();
		List<ItemEntity> tr = new ArrayList<ItemEntity>();
		
		Iterator<ItemEntity> it = e.getDrops().iterator();
		while (it.hasNext()) {
			ItemEntity ie = it.next();
			ItemStack stack = ie.getItem();
			Item item = stack.getItem();
			if (alldrops.contains(item)) {
				tr.add(ie);
			}
		}
		
		if (tr.size() > 0) {
			for (ItemEntity ie : tr) {
				drops.remove(ie);
			}
		}

		for (Item item : alldrops) {
			ItemEntity ie = new ItemEntity(world, evec.x, evec.y+1, evec.z, new ItemStack(item, amount));
			drops.add(ie);
		}
	}
}
