/*
 * This is the latest source code of Eroding Stone Entities.
 * Minecraft version: 1.19.1, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.erodingstoneentities.events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.erodingstoneentities.config.ConfigHandler;
import com.natamus.erodingstoneentities.util.Util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EntityEvent {
	private static HashMap<Level, Integer> worldtickcount = new HashMap<Level, Integer>();
	private static HashMap<Level, CopyOnWriteArrayList<ItemEntity>> perworldentities = new HashMap<Level, CopyOnWriteArrayList<ItemEntity>>();
	private static HashMap<ItemEntity, Integer> iecount = new HashMap<ItemEntity, Integer>();
	
	@SubscribeEvent
	public void onWorldTick(LevelTickEvent e) {
		Level world = e.level;
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
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
				BlockPos iepos = ie.blockPosition();
				BlockState ieposstate = world.getBlockState(iepos);
				if (ieposstate.getBlock().equals(Blocks.WATER)) {
					int level = ieposstate.getValue(LiquidBlock.LEVEL);
					if (level > 0) { // flowing
						if (ConfigHandler.GENERAL.preventErosionIfAboveIceBlock.get()) {
							Block belowblock = world.getBlockState(iepos.below()).getBlock();
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
	public void onEntityJoin(EntityJoinLevelEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
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
