/*
 * This is the latest source code of Eroding Stone Entities.
 * Minecraft version: 1.16.5, mod version: 2.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Eroding Stone Entities ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.erodingstoneentities.events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.erodingstoneentities.config.ConfigHandler;
import com.natamus.erodingstoneentities.util.Util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	private static HashMap<World, Integer> worldtickcount = new HashMap<World, Integer>();
	private static HashMap<World, CopyOnWriteArrayList<ItemEntity>> perworldentities = new HashMap<World, CopyOnWriteArrayList<ItemEntity>>();
	private static HashMap<ItemEntity, Integer> iecount = new HashMap<ItemEntity, Integer>();
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		World world = e.world;
		if (world.isRemote || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (!worldtickcount.containsKey(world)) {
			worldtickcount.put(world, 1);
			return;
		}
		
		int currenttick = worldtickcount.get(world);
		if (currenttick % 20 != 0) {
			worldtickcount.put(world, currenttick+1);
			return;
		}
		worldtickcount.put(world, 1);
		
		if (!perworldentities.containsKey(world)) {
			return;
		}
		
		if (perworldentities.get(world).size() > 0) {
			for (ItemEntity ie : perworldentities.get(world)) {
				if (!ie.isAlive()) {
					perworldentities.get(world).remove(ie);
					iecount.remove(ie);
					continue;
				}
				
				int timeleft = iecount.get(ie);
				BlockPos iepos = ie.getPosition();
				BlockState ieposstate = world.getBlockState(iepos);
				if (ieposstate.getBlock().equals(Blocks.WATER)) {
					int level = ieposstate.get(FlowingFluidBlock.LEVEL);
					if (level > 0) { // flowing
						if (ConfigHandler.GENERAL.preventErosionIfAboveIceBlock.get()) {
							Block belowblock = world.getBlockState(iepos.down()).getBlock();
							if (Util.isIceBlock(belowblock)) {
								continue;
							}
						}
						
						timeleft-=1;
						if (timeleft == 0) {
							perworldentities.get(world).remove(ie);
							iecount.remove(ie);
							Util.transformItemEntity(world, ie);
							continue;
						}
						else {
							iecount.put(ie, timeleft);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof ItemEntity == false) {
			return;
		}
		
		ItemEntity ie = (ItemEntity)entity;
		ItemStack stack = ie.getItem();
		if (!Util.erodeinto.containsKey(stack.getItem())) {
			return;
		}
		
		if (!perworldentities.containsKey(world)) {
			perworldentities.put(world, new CopyOnWriteArrayList<ItemEntity>(Arrays.asList(ie)));
		}
		else {
			perworldentities.get(world).add(ie);
		}
		
		iecount.put(ie, ConfigHandler.GENERAL.durationInSecondsStoneErodes.get());
	}
}
