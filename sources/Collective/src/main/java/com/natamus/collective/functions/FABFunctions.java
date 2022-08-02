/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.37.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective.functions;

import com.natamus.collective.config.ConfigHandler;
import com.natamus.collective.data.GlobalVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class FABFunctions {
	private static final Map<Block, Map<Level, List<BlockPos>>> getMapFromBlock = new HashMap<Block, Map<Level, List<BlockPos>>>();
	private static final Map<Level, Map<Date, BlockPos>> timeoutpositions = new HashMap<Level, Map<Date, BlockPos>>();
	
	private static List<BlockEntity> getBlockEntitiesAroundPosition(Level world, BlockPos pos, Integer radius) {
		List<BlockEntity> blockentities = new ArrayList<BlockEntity>();
		
		int chunkradius = (int)Math.ceil(radius/16.0);
	    int chunkPosX = pos.getX() >> 4;
	    int chunkPosZ = pos.getZ() >> 4;

	    for (int x = chunkPosX - chunkradius; x < chunkPosX + chunkradius; x++) {
	    	for (int z = chunkPosZ - chunkradius; z < chunkPosZ + chunkradius; z++) {
				for (BlockEntity be : world.getChunk(x, z).getBlockEntities().values()) {
					if (!blockentities.contains(be)) {
						blockentities.add(be);
					}
				}
	    	}
	    }
		
		return blockentities;
	}
	
	public static List<BlockPos> getAllTileEntityPositionsNearbyEntity(BlockEntityType<?> tetype, Integer radius,  Level world, Entity entity) {
		BlockPos entitypos = entity.blockPosition();
		
		List<BlockPos> nearbypositions = new ArrayList<BlockPos>();
		List<BlockEntity> blockentities = getBlockEntitiesAroundPosition(world, entitypos, radius);
		
		for (BlockEntity loadedtileentity : blockentities) {
			BlockEntityType<?> loadedtiletype = loadedtileentity.getType();
			if (loadedtiletype == null) {
				continue;
			}
			
			if (loadedtiletype.equals(tetype)) {
				BlockPos ltepos = loadedtileentity.getBlockPos();
				Vec3 vec3 = entity.position();
				if (ltepos.closerThan(new Vec3i(vec3.x, vec3.y, vec3.z), radius)) {
					nearbypositions.add(loadedtileentity.getBlockPos());
				}
			}
		}
		
		return nearbypositions;
	}
	
	public static BlockPos getRequestedBlockAroundEntitySpawn(Block rawqueryblock, Integer radius, Double radiusmodifier, Level world, Entity entity) {
		Block requestedblock = processCommonBlock(rawqueryblock);
		Map<Level, List<BlockPos>> worldblocks = getMap(requestedblock);

		BlockPos epos = entity.blockPosition();
		
		List<BlockPos> currentblocks;
		BlockPos removeblockpos = null;
		
		if (worldblocks.containsKey(world)) {
			currentblocks = worldblocks.get(world);
			
			List<BlockPos> cbtoremove = new ArrayList<BlockPos>(); 
			for (BlockPos cblock : currentblocks) {
				if (!world.getChunkSource().hasChunk(cblock.getX() >> 4, cblock.getZ() >> 4)) {
					cbtoremove.add(cblock);
					continue;
				}
				if (!world.getBlockState(cblock).getBlock().equals(requestedblock)) {
					cbtoremove.add(cblock);
					continue;
				}
				
				if (cblock.closerThan(epos, radius*radiusmodifier)) {
					return cblock.immutable();
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
						if (toepos.closerThan(removeblockpos, 64)) {
							totoremove.add(todate);
						}
					}
					long ms = (now.getTime()-todate.getTime());
					if (ms > ConfigHandler.COLLECTIVE.findABlockcheckAroundEntitiesDelayMs.get()) {
						totoremove.add(todate);
						continue;
					}
					if (toepos.closerThan(epos, radius*radiusmodifier)) {
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
			List<BlockEntity> blockentities = getBlockEntitiesAroundPosition(world, epos, radius);
			BlockEntityType<?> tiletypetofind = GlobalVariables.blocksWithTileEntity.get(requestedblock);
			
			for (BlockEntity loadedtileentity : blockentities) {
				BlockEntityType<?> loadedtiletype = loadedtileentity.getType();
				if (loadedtiletype == null) {
					continue;
				}
				
				if (loadedtiletype.equals(tiletypetofind)) {
					BlockPos ltepos = loadedtileentity.getBlockPos();
				
					if (ltepos.closerThan(epos, radius*radiusmodifier)) {
						currentblocks.add(ltepos.immutable());
						worldblocks.put(world, currentblocks);
						getMapFromBlock.put(requestedblock, worldblocks);
						
						return ltepos.immutable();
					}
				}
			}
		}
		else {
			int r = radius;
			for (int x = -r; x < r; x++) {
				for (int y = -r; y < r; y++) {
					for (int z = -r; z < r; z++) {
						BlockPos cpos = epos.east(x).north(y).above(z);
						BlockState state = world.getBlockState(cpos);
						if (state.getBlock().equals(requestedblock)) {
							currentblocks.add(cpos.immutable());
							worldblocks.put(world, currentblocks);
							getMapFromBlock.put(requestedblock, worldblocks);
							
							return cpos.immutable();
						}
					}
				}
			}		
		}
		
		timeouts.put(new Date(), epos.immutable());
		timeoutpositions.put(world, timeouts);
		return null;
	}
	
	public static BlockPos updatePlacedBlock(Block requestedblock, BlockPos bpos, Level world) {
		BlockState state = world.getBlockState(bpos);
		if (state.getBlock().equals(requestedblock)) {
			Map<Level, List<BlockPos>> worldblocks = getMap(requestedblock);
			
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
	private static Map<Level,List<BlockPos>> getMap(Block requestedblock) {
		Map<Level,List<BlockPos>> worldblocks;
		if (getMapFromBlock.containsKey(requestedblock)) {
			worldblocks = getMapFromBlock.get(requestedblock);
		}
		else {
			worldblocks = new HashMap<Level, List<BlockPos>>();
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
