/*
 * This is the latest source code of Scaffolding Drops Nearby.
 * Minecraft version: 1.19.2, mod version: 1.8.
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

package com.natamus.scaffoldingdropsnearby.events;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ScaffoldingEvent {
	private static CopyOnWriteArrayList<BlockPos> lastScaffoldings = new CopyOnWriteArrayList<BlockPos>();
	private static HashMap<BlockPos, Date> lastaction = new HashMap<BlockPos, Date>();
	
	@SubscribeEvent
	public void onScaffoldingItem(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof ItemEntity == false) {
			return;	
		}
		
		ItemEntity ie = (ItemEntity)entity;
		ItemStack itemstack = ie.getItem();
		if (itemstack.getItem() instanceof ScaffoldingBlockItem == false) {
			return;
		}
		
		Date now = new Date();
		
		BlockPos scafpos = entity.blockPosition();
		BlockPos lowscafpos = new BlockPos(scafpos.getX(), 1, scafpos.getZ());
		for (BlockPos lspos : lastScaffoldings) {
			if (lastaction.containsKey(lspos)) {
				Date lastdate = lastaction.get(lspos);
				long ms = (now.getTime()-lastdate.getTime());
				if (ms > 2000) {
					lastScaffoldings.remove(lspos);
					lastaction.remove(lspos);
					continue;
				}			
			}
			
			if (lowscafpos.closerThan(new BlockPos(lspos.getX(), 1, lspos.getZ()), 20)) {
				entity.teleportTo(lspos.getX(), lspos.getY()+1, lspos.getZ());
				lastaction.put(lspos.immutable(), now);
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		BlockState state = e.getState();
		Block block = state.getBlock();
		if (block.equals(Blocks.SCAFFOLDING)) {
			BlockPos pos = e.getPos().immutable();
			lastScaffoldings.add(pos);
			lastaction.put(pos, new Date());
		}
	}
}
