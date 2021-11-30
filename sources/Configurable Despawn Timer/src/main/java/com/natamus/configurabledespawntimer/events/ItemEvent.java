/*
 * This is the latest source code of Configurable Despawn Timer.
 * Minecraft version: 1.18.0, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Configurable Despawn Timer ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.configurabledespawntimer.events;

import java.util.ArrayList;
import java.util.List;

import com.natamus.configurabledespawntimer.config.ConfigHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ItemEvent {
	private static List<ItemEntity> itemstocheck = new ArrayList<ItemEntity>();
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent e) {
		if (!e.phase.equals(Phase.END)) {
			return;
		}
		
		if (itemstocheck.size() > 0) {
			while (itemstocheck.size() > 0) {
				ItemEntity ie = itemstocheck.get(0);
				if (ie.isAlive()) {
					CompoundTag nbtc = ie.serializeNBT();
					int age = nbtc.getShort("Age");
					if (age >= 5990) {
						ie.remove(RemovalReason.DISCARDED);
					}
				}
				
				itemstocheck.remove(0);
			}
		}
	}
	
	@SubscribeEvent
	public void onItemJoinWorld(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof ItemEntity == false) {
			return;
		}
			
		ItemEntity ie = (ItemEntity)entity;
		if (ie.lifespan != 6000) {
			return;
		}
		
		CompoundTag nbtc = ie.serializeNBT();
		int age = nbtc.getShort("Age");
		
		ie.lifespan = ConfigHandler.GENERAL.despawnTimeInTicks.get();
		
		if (age < 10) {
			itemstocheck.add(ie);
		}
	}
}
