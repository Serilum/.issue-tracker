/*
 * This is the latest source code of Scaffolding Drops Nearby.
 * Minecraft version: 1.18.1, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Scaffolding Drops Nearby ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ScaffoldingEvent {
	private static CopyOnWriteArrayList<BlockPos> lastScaffoldings = new CopyOnWriteArrayList<BlockPos>();
	private static HashMap<BlockPos, Date> lastaction = new HashMap<BlockPos, Date>();
	
	@SubscribeEvent
	public void onScaffoldingItem(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
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
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
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
