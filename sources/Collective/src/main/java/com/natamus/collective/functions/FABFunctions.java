/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.16.5, mod version: 2.26.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.natamus.collective.config.ConfigHandler;
import com.natamus.collective.data.GlobalVariables;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FABFunctions {
	private static Map<Block, Map<World, List<BlockPos>>> getMapFromBlock = new HashMap<Block, Map<World, List<BlockPos>>>();
	private static Map<World, Map<Date, BlockPos>> timeoutpositions = new HashMap<World, Map<Date, BlockPos>>();
	
	public static List<BlockPos> getAllTileEntityPositionsNearbyEntity(TileEntityType<?> tetype, Integer radius,  World world, Entity entity) {
		List<BlockPos> nearbypositions = new ArrayList<BlockPos>();
		for (TileEntity loadedtileentity : world.loadedTileEntityList) {
			TileEntityType<?> loadedtiletype = loadedtileentity.getType();
			if (loadedtiletype == null) {
				continue;
			}
			
			if (loadedtiletype.equals(tetype)) {
				BlockPos ltepos = loadedtileentity.getPos();
				if (ltepos.withinDistance(entity.getPositionVec(), radius)) {
					nearbypositions.add(loadedtileentity.getPos());
				}
			}
		}
		
		return nearbypositions;
	}
	
	public static BlockPos getRequestedBlockAroundEntitySpawn(Block rawqueryblock, Integer radius, Double radiusmodifier, World world, Entity entity) {
		Block requestedblock = processCommonBlock(rawqueryblock);
		Map<World,List<BlockPos>> worldblocks = getMap(requestedblock);

		BlockPos epos = entity.getPosition();
		
		List<BlockPos> currentblocks;
		BlockPos removeblockpos = null;
		
		if (worldblocks.containsKey(world)) {
			currentblocks = worldblocks.get(world);
			
			List<BlockPos> cbtoremove = new ArrayList<BlockPos>(); 
			for (BlockPos cblock : currentblocks) {
				if (!world.getChunkProvider().chunkExists(cblock.getX() >> 4, cblock.getZ() >> 4)) {
					cbtoremove.add(cblock);
					continue;
				}
				if (!world.getBlockState(cblock).getBlock().equals(requestedblock)) {
					cbtoremove.add(cblock);
					continue;
				}
				
				if (cblock.withinDistance(epos, radius*radiusmodifier)) {
					removeblockpos = cblock.toImmutable();
					return cblock.toImmutable();
				}
			}
			
			if (cbtoremove.size() > 0) {
				for (BlockPos tr : cbtoremove) {
					currentblocks.remove(tr);
				}
			}
		}
		else {
			currentblocks = new ArrayList<BlockPos>();
		}
		
		// Timeout function which prevents too many of the loop through blocks.
		Map<Date, BlockPos> timeouts;
		if (timeoutpositions.containsKey(world)) {
			timeouts = timeoutpositions.get(world);
			
			List<Date> totoremove = new ArrayList<Date>(); 
			if (timeouts.size() > 0) {
				Date now = new Date();
				for (Date todate : timeouts.keySet()) {
					BlockPos toepos = timeouts.get(todate);
					if (removeblockpos != null) {
						if (toepos.withinDistance(removeblockpos, 64)) {
							totoremove.add(todate);
						}
					}
					long ms = (now.getTime()-todate.getTime());
					if (ms > ConfigHandler.COLLECTIVE.findABlockcheckAroundEntitiesDelayMs.get()) {
						totoremove.add(todate);
						continue;
					}
					if (toepos.withinDistance(epos, radius*radiusmodifier)) {
						return null;
					}
				}
			}
			
			if (totoremove.size() > 0) {
				for (Date tr : totoremove) {
					timeouts.remove(tr);
				}
			}
		}
		else {
			timeouts = new HashMap<Date, BlockPos>();
		}
		
		if (GlobalVariables.blocksWithTileEntity.containsKey(requestedblock)) {
			TileEntityType<?> tiletypetofind = GlobalVariables.blocksWithTileEntity.get(requestedblock);
			
			List<TileEntity> loadedtileentities = world.loadedTileEntityList;
			for (TileEntity loadedtileentity : loadedtileentities) {
				TileEntityType<?> loadedtiletype = loadedtileentity.getType();
				if (loadedtiletype == null) {
					continue;
				}
				
				if (loadedtiletype.equals(tiletypetofind)) {
					BlockPos ltepos = loadedtileentity.getPos();
				
					if (ltepos.withinDistance(epos, radius*radiusmodifier)) {
						currentblocks.add(ltepos.toImmutable());
						worldblocks.put(world, currentblocks);
						getMapFromBlock.put(requestedblock, worldblocks);
						
						return ltepos.toImmutable();
					}
				}
			}
		}
		else {
			int r = radius;
			for (int x = -r; x < r; x++) {
				for (int y = -r; y < r; y++) {
					for (int z = -r; z < r; z++) {
						BlockPos cpos = epos.east(x).north(y).up(z);
						BlockState state = world.getBlockState(cpos);
						if (state.getBlock().equals(requestedblock)) {
							currentblocks.add(cpos.toImmutable());
							worldblocks.put(world, currentblocks);
							getMapFromBlock.put(requestedblock, worldblocks);
							
							return cpos.toImmutable();
						}
					}
				}
			}		
		}
		
		timeouts.put(new Date(), epos.toImmutable());
		timeoutpositions.put(world, timeouts);
		return null;
	}
	
	public static BlockPos updatePlacedBlock(Block requestedblock, BlockPos bpos, World world) {
		BlockState state = world.getBlockState(bpos);
		if (state.getBlock().equals(requestedblock)) {
			Map<World, List<BlockPos>> worldblocks = getMap(requestedblock);
			
			List<BlockPos> currentblocks;
			if (worldblocks.containsKey(world)) {
				currentblocks = worldblocks.get(world);
			}
			else {
				currentblocks = new ArrayList<BlockPos>();
			}
			
			if (!currentblocks.contains(bpos)) {
				currentblocks.add(bpos);
				worldblocks.put(world, currentblocks);
				getMapFromBlock.put(requestedblock, worldblocks);
			}
			return bpos;
		}
		
		return null;
	}
	
	// Internal util functions
	private static Map<World,List<BlockPos>> getMap(Block requestedblock) {
		Map<World,List<BlockPos>> worldblocks;
		if (getMapFromBlock.containsKey(requestedblock)) {
			worldblocks = getMapFromBlock.get(requestedblock);
		}
		else {
			worldblocks = new HashMap<World, List<BlockPos>>();
		}
		return worldblocks;
	}
	
	private static Block processCommonBlock(Block requestedblock) {
		Block blocktoreturn = requestedblock;
		if (requestedblock instanceof StandingSignBlock || requestedblock instanceof WallSignBlock) {
			blocktoreturn = Blocks.OAK_SIGN;
		}
		
		return blocktoreturn;
	}
}
