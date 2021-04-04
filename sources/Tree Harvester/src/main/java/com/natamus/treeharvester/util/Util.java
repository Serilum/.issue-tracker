/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.16.5, mod version: 2.4.
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
import com.natamus.treeharvester.config.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Util {
	public static HashMap<BlockPos, Integer> highestleaf = new HashMap<BlockPos, Integer>();
	public static CopyOnWriteArrayList<Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>> lowerlogs = new CopyOnWriteArrayList<Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>>();
	
	public static int isTreeAndReturnLogAmount(World world, BlockPos pos) {
		highestleaf.put(pos, 0);
		
		int leafcount = 20;
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
			
			Iterator<BlockPos> it = BlockPos.getAllInBox(pos.getX()-2, pos.getY()+(y-1), pos.getZ()-2, pos.getX()+2, pos.getY()+(y-1), pos.getZ()+2).iterator();
			while (it.hasNext()) {
				BlockPos npos = it.next();
				Block nblock = world.getBlockState(npos).getBlock();
				if (isTreeLeaf(nblock)) {
					leafcount-=1;
					if (npos.getY() > highesty) {
						highesty = npos.getY();
					}
				}
				else if (isTreeLog(nblock)) {
					logcount+=1;
				}
			}
		}
		
		highestleaf.put(pos.toImmutable(), highesty);
		
		if (leafcount < 0) {
			return logcount;
		}
		return -1;
	}
	
	public static boolean isTreeLeaf(Block block) {
		if (block instanceof LeavesBlock || block instanceof BushBlock) {
			return true;
		}
		return false;
	}
	
	public static boolean isTreeLog(Block block) {
		if (block instanceof RotatedPillarBlock) {
			return true;
		}
		return false;
	}
	
	public static boolean isSapling(ItemStack itemstack) {
		Item item = itemstack.getItem();
		if (Block.getBlockFromItem(item) instanceof SaplingBlock) {
			return true;
		}
		return false;
	}
	
	public static List<BlockPos> getAllLogsToBreak(World world, BlockPos pos, int logcount) {
		CopyOnWriteArrayList<BlockPos> bottomlogs = new CopyOnWriteArrayList<BlockPos>();
		if (ConfigHandler.GENERAL.replaceSaplingIfBottomLogIsBroken.get()) {
			if (world.getBlockState(pos.down()).getBlock().equals(Blocks.DIRT)) {
				Iterator<BlockPos> it = BlockPos.getAllInBox(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY(), pos.getZ()+1).iterator();
				while (it.hasNext()) {
					BlockPos npos = it.next();
					Block block = world.getBlockState(npos).getBlock();
					if (isTreeLog(block)) {
						bottomlogs.add(npos.toImmutable());
					}
				}
			}
		}
		
		if (ConfigHandler.GENERAL.replaceSaplingIfBottomLogIsBroken.get()) {
			if (ConfigHandler.GENERAL.instantBreakLeavesAround.get()) {
				replaceSapling(world, pos, bottomlogs, 1);
			}
			else if (ConfigHandler.GENERAL.enableFastLeafDecay.get()){
				lowerlogs.add(new Pair<BlockPos, CopyOnWriteArrayList<BlockPos>>(pos.toImmutable(), bottomlogs));
			}
		}
		
		return getLogsToBreak(world, pos, new ArrayList<BlockPos>(), logcount);
	}
	
	public static void replaceSapling(World world, BlockPos pos, CopyOnWriteArrayList<BlockPos> bottomlogs, int radius) {
    	int reducecount = bottomlogs.size();
    	int rc = reducecount;
		ItemStack sapling = null;
		
		Iterator<Entity> entitiesaround = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.getX()-radius, pos.getY()-2, pos.getZ()-radius, pos.getX()+radius, pos.getY()+30, pos.getZ()+radius)).iterator();
		while (entitiesaround.hasNext()) {
			Entity ea = entitiesaround.next();
			if (ea instanceof ItemEntity) {
				ItemEntity eia = (ItemEntity)ea;
				ItemStack eisa = eia.getItem();
				if (isSapling(eisa)) {
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
						eia.remove();;
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
			
			world.setBlockState(bottompos, Block.getBlockFromItem(sapling.getItem()).getDefaultState());
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
	
	private static List<BlockPos> getLogsToBreak(World world, BlockPos pos, List<BlockPos> logstobreak, int logcount) {
		List<BlockPos> checkaround = new ArrayList<BlockPos>();
		
		Iterator<BlockPos> aroundlogs = BlockPos.getAllInBox(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1).iterator();
		while (aroundlogs.hasNext()) {
			BlockPos nalogpos = aroundlogs.next().toImmutable();
			if (logstobreak.contains(nalogpos)) {
				continue;
			}
			BlockState logstate = world.getBlockState(nalogpos);
			Block logblock = logstate.getBlock();
			if (isTreeLog(logblock)) {
				checkaround.add(nalogpos);
				logstobreak.add(nalogpos);
				
				Pair<Integer, Integer> hv = getHorizontalAndVerticalValue(logcount);
				int h = hv.getFirst();
				int v = hv.getSecond();

				Iterator<BlockPos> aroundleaves = BlockPos.getAllInBox(pos.getX()-h, pos.getY(), pos.getZ()-h, pos.getX()+h, pos.getY()+v, pos.getZ()+h).iterator();
				while (aroundleaves.hasNext()) {
					BlockPos naleafpos = aroundleaves.next();
					Block leafblock = world.getBlockState(naleafpos).getBlock();
					if (isTreeLeaf(leafblock)) {
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
			for (BlockPos logpos : getLogsToBreak(world, capos, logstobreak, logcount)) {
				if (!logstobreak.contains(logpos)) {
					logstobreak.add(logpos.toImmutable());
				}
			}
		}
		
		BlockPos up = pos.up(2);
		return getLogsToBreak(world, up.toImmutable(), logstobreak, logcount);
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