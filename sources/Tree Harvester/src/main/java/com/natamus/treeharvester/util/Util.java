/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.18.1, mod version: 4.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Tree Harvester ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.treeharvester.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.CompareBlockFunctions;
import com.natamus.collective.functions.CompareItemFunctions;
import com.natamus.treeharvester.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class Util {
	public static HashMap<BlockPos, Integer> highestleaf = new HashMap<BlockPos, Integer>();
	public static CopyOnWriteArrayList<Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>> lowerlogs = new CopyOnWriteArrayList<Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>>();
	
	public static int isTreeAndReturnLogAmount(Level world, BlockPos pos) {
		highestleaf.put(pos, 0);
		
		int leafcount = 10;
		int logcount = 0;
		int prevleafcount = -1;
		int prevlogcount = -1;
	
		int highesty = 0;
		for (int y = 1; y<=30; y+=1) {
			if (prevleafcount == leafcount && prevlogcount == logcount) {
				break;
			}
			prevleafcount = leafcount;
			prevlogcount = logcount;
			
			Iterator<BlockPos> it = BlockPos.betweenClosedStream(pos.getX()-2, pos.getY()+(y-1), pos.getZ()-2, pos.getX()+2, pos.getY()+(y-1), pos.getZ()+2).iterator();
			while (it.hasNext()) {
				BlockPos npos = it.next();
				Block nblock = world.getBlockState(npos).getBlock();
				if (CompareBlockFunctions.isTreeLeaf(nblock, ConfigHandler.GENERAL.enableNetherTrees.get())) {
					leafcount-=1;
					if (npos.getY() > highesty) {
						highesty = npos.getY();
					}
				}
				else if (CompareBlockFunctions.isTreeLog(nblock)) {
					logcount+=1;
				}
			}
		}
		
		highestleaf.put(pos.immutable(), highesty);
		
		if (leafcount < 0) {
			return logcount;
		}
		return -1;
	}
	
	public static List<BlockPos> getAllLogsToBreak(Level world, BlockPos pos, int logcount, Block logtype) {
		CopyOnWriteArrayList<BlockPos> bottomlogs = new CopyOnWriteArrayList<BlockPos>();
		if (ConfigHandler.GENERAL.replaceSaplingIfBottomLogIsBroken.get()) {
			Block blockbelow = world.getBlockState(pos.below()).getBlock();
			if (CompareBlockFunctions.isDirtBlock(blockbelow)) {
				Iterator<BlockPos> it = BlockPos.betweenClosedStream(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY(), pos.getZ()+1).iterator();
				while (it.hasNext()) {
					BlockPos npos = it.next();
					Block block = world.getBlockState(npos).getBlock();
					if (block.equals(logtype)) {
						bottomlogs.add(npos.immutable());
					}
				}
			}
		}
		
		if (ConfigHandler.GENERAL.replaceSaplingIfBottomLogIsBroken.get()) {
			if (ConfigHandler.GENERAL.instantBreakLeavesAround.get()) {
				replaceSapling(world, pos, bottomlogs, 1);
			}
			else if (ConfigHandler.GENERAL.enableFastLeafDecay.get()){
				lowerlogs.add(new Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>(pos.immutable(), bottomlogs));
			}
		}
		
		return getLogsToBreak(world, pos, new ArrayList<BlockPos>(), logcount, logtype);
	}
	
	public static void replaceSapling(Level world, BlockPos pos, CopyOnWriteArrayList<BlockPos> bottomlogs, int radius) {
    	int reducecount = bottomlogs.size();
    	int rc = reducecount;
		ItemStack sapling = null;
		
		Iterator<Entity> entitiesaround = world.getEntities(null, new AABB(pos.getX()-radius, pos.getY()-2, pos.getZ()-radius, pos.getX()+radius, pos.getY()+30, pos.getZ()+radius)).iterator();
		while (entitiesaround.hasNext()) {
			Entity ea = entitiesaround.next();
			if (ea instanceof ItemEntity) {
				ItemEntity eia = (ItemEntity)ea;
				ItemStack eisa = eia.getItem();
				if (CompareItemFunctions.isSapling(eisa)) {
					if (sapling == null) {
						sapling = eisa.copy();
					}
					
					int count = eisa.getCount();
					if (count > 1) {
						for (int n = 0; n < count; n++) {
							eisa.shrink(1);
							rc-=1;
							
							if (rc == 0) {
								break;
							}
						}
						eia.setItem(eisa);
					}
					else {
						rc-=1;
						eia.remove(RemovalReason.DISCARDED);
					}
					
					if (rc == 0) {
						break;
					}
				}
			}
		}
    	
		int setsaplings = bottomlogs.size()-rc;
		for (BlockPos bottompos : bottomlogs) {
			if (setsaplings == 0) {
				break;
			}
			
			world.setBlockAndUpdate(bottompos, Block.byItem(sapling.getItem()).defaultBlockState());
			setsaplings-=1;
			bottomlogs.remove(bottompos);
		}
		
		if (bottomlogs.size() > 0) {
			if (radius >= 5) {
				return;
			}
			replaceSapling(world, pos, bottomlogs, radius+2);
		}
	}
	
	private static List<BlockPos> getLogsToBreak(Level world, BlockPos pos, List<BlockPos> logstobreak, int logcount, Block logtype) {
		List<BlockPos> checkaround = new ArrayList<BlockPos>();
		
		Iterator<BlockPos> aroundlogs = BlockPos.betweenClosedStream(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1).iterator();
		while (aroundlogs.hasNext()) {
			BlockPos nalogpos = aroundlogs.next().immutable();
			if (logstobreak.contains(nalogpos)) {
				continue;
			}
			BlockState logstate = world.getBlockState(nalogpos);
			Block logblock = logstate.getBlock();
			if (logblock.equals(logtype)) {
				checkaround.add(nalogpos);
				logstobreak.add(nalogpos);
				
				Pair<Integer, Integer> hv = getHorizontalAndVerticalValue(logcount);
				int h = hv.getFirst();
				int v = hv.getSecond();

				Iterator<BlockPos> aroundleaves = BlockPos.betweenClosedStream(pos.getX()-h, pos.getY(), pos.getZ()-h, pos.getX()+h, pos.getY()+v, pos.getZ()+h).iterator();
				while (aroundleaves.hasNext()) {
					BlockPos naleafpos = aroundleaves.next();
					Block leafblock = world.getBlockState(naleafpos).getBlock();
					if (CompareBlockFunctions.isTreeLeaf(leafblock, ConfigHandler.GENERAL.enableNetherTrees.get())) {
						if (ConfigHandler.GENERAL.instantBreakLeavesAround.get()) {
							world.destroyBlock(naleafpos, true);
						}
					}
				}
			}
		}
		
		if (checkaround.size() == 0) {
			return logstobreak;
		}
		
		for (BlockPos capos : checkaround) {
			for (BlockPos logpos : getLogsToBreak(world, capos, logstobreak, logcount, logtype)) {
				if (!logstobreak.contains(logpos)) {
					logstobreak.add(logpos.immutable());
				}
			}
		}
		
		BlockPos up = pos.above(2);
		return getLogsToBreak(world, up.immutable(), logstobreak, logcount, logtype);
	}
	
	public static Pair<Integer, Integer> getHorizontalAndVerticalValue(int logcount) {
		int h = 3; // horizontal
		int v = 4; // vertical
		if (logcount >= 20) {
			h = 5;
			v = 5;	
		}
		else if (logcount >= 10) {
			h = 4;
			v = 5;
		}
		
		return new Pair<Integer, Integer>(h, v);
	}
}