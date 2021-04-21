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
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

public class BlockPosFunctions {
	// START: GET functions
	public static List<BlockPos> getBlocksAround(BlockPos pos, boolean down) {
		List<BlockPos> around = new ArrayList<BlockPos>();
		around.add(pos.north());
		around.add(pos.east());
		around.add(pos.south());
		around.add(pos.west());
		around.add(pos.up());
		if (down) {
			around.add(pos.down());
		}
		return around;
	}
	
	// START: RECURSIVE GET BLOCKS
	public static List<BlockPos> getBlocksNextToEachOther(World world, BlockPos startpos, List<Block> possibleblocks) {
		List<BlockPos> checkedblocks = new ArrayList<BlockPos>();
		List<BlockPos> theblocksaround = new ArrayList<BlockPos>();
		if (possibleblocks.contains(world.getBlockState(startpos).getBlock())) {
			theblocksaround.add(startpos);
			checkedblocks.add(startpos);
		}
		
		recursiveGetNextBlocks(world, startpos, possibleblocks, theblocksaround, checkedblocks);
		return theblocksaround;
	}
	private static void recursiveGetNextBlocks(World world, BlockPos pos, List<Block> possibleblocks, List<BlockPos> theblocksaround, List<BlockPos> checkedblocks) {
		List<BlockPos> possibleblocksaround = getBlocksAround(pos, true);
		for (BlockPos pba : possibleblocksaround) {
			if (checkedblocks.contains(pba)) {
				continue;
			}
			checkedblocks.add(pba);
			
			if (possibleblocks.contains(world.getBlockState(pba).getBlock())) {
				if (!theblocksaround.contains(pba)) {
					theblocksaround.add(pba);
					recursiveGetNextBlocks(world, pba, possibleblocks, theblocksaround, checkedblocks);
				}
			}
		}
	}
	public static List<BlockPos> getBlocksNextToEachOtherMaterial(World world, BlockPos startpos, List<Material> possiblematerials) {
		List<BlockPos> checkedblocks = new ArrayList<BlockPos>();
		List<BlockPos> theblocksaround = new ArrayList<BlockPos>();
		if (possiblematerials.contains(world.getBlockState(startpos).getMaterial())) {
			theblocksaround.add(startpos);
			checkedblocks.add(startpos);
		}
		
		recursiveGetNextBlocksMaterial(world, startpos, possiblematerials, theblocksaround, checkedblocks);
		return theblocksaround;
	}
	private static void recursiveGetNextBlocksMaterial(World world, BlockPos pos, List<Material> possiblematerials, List<BlockPos> theblocksaround, List<BlockPos> checkedblocks) {
		List<BlockPos> possibleblocksaround = getBlocksAround(pos, true);
		for (BlockPos pba : possibleblocksaround) {
			if (checkedblocks.contains(pba)) {
				continue;
			}
			checkedblocks.add(pba);
			
			if (possiblematerials.contains(world.getBlockState(pba).getMaterial())) {
				if (!theblocksaround.contains(pba)) {
					theblocksaround.add(pba);
					recursiveGetNextBlocksMaterial(world, pba, possiblematerials, theblocksaround, checkedblocks);
				}
			}
		}
	}
	// END RECURSIVE GET BLOCKS
	
	public static BlockPos getSurfaceBlockPos(ServerWorld serverworld, int x, int z) {
		BlockPos returnpos = new BlockPos(x, 255, z);
		if (!WorldFunctions.isNether(serverworld)) {
			int maxheight = 256;
			BlockPos pos = new BlockPos(x, maxheight, z);
			for (int y = maxheight; y > 0; y--) {
				BlockState blockstate = serverworld.getBlockState(pos);
				Material material = blockstate.getMaterial();
				if (blockstate.getOpacity(serverworld, pos) >= 15 || material.equals(Material.ICE) || material.equals(Material.PACKED_ICE)) {
					returnpos = pos.up().toImmutable();
					break;
				}
				
				pos = pos.down();
			}
		}
		else {
			int maxheight = 128;
			BlockPos pos = new BlockPos(x, 0, z);
			for (int y = 0; y < maxheight; y++) {
				BlockState blockstate = serverworld.getBlockState(pos);
				if (blockstate.getBlock().equals(Blocks.AIR)) {
					BlockState upstate = serverworld.getBlockState(pos.up());
					if (upstate.getBlock().equals(Blocks.AIR)) {
						returnpos = pos.toImmutable();
						break;
					}
				}
				
				pos = pos.up();
			}
		}
		
		return returnpos;
	}
	
	public static BlockPos getCenterNearbyVillage(ServerWorld serverworld) {
		return getNearbyVillage(serverworld, new BlockPos(0, 0, 0));
	}
	public static BlockPos getNearbyVillage(ServerWorld serverworld, BlockPos nearpos) {
		return getNearbyStructure(serverworld, Structure.VILLAGE, nearpos, 9999);
	}
	
	public static BlockPos getCenterNearbyStructure(ServerWorld serverworld, Structure<?> structure) {
		return getNearbyStructure(serverworld, structure, new BlockPos(0, 0, 0));
	}
	public static BlockPos getNearbyStructure(ServerWorld serverworld, Structure<?> structure, BlockPos nearpos) {
		return getNearbyStructure(serverworld, structure, nearpos, 9999);
	}	
	public static BlockPos getNearbyStructure(ServerWorld serverworld, Structure<?> structure, BlockPos nearpos, int radius) {
		BlockPos villagepos = serverworld.func_241117_a_(structure, nearpos, radius, false);
		if (villagepos == null) {
			return null;
		}
		
		BlockPos spawnpos = null;
		for (int y = 255; y > 0; y--) {
			BlockPos checkpos = new BlockPos(villagepos.getX(), y, villagepos.getZ());
			if (serverworld.getBlockState(checkpos).getBlock().equals(Blocks.AIR)) {
				continue;
			}
			spawnpos = checkpos.up().toImmutable();
			break;
		}
		
		return spawnpos;
	}
	
	public static BlockPos getCenterBiome(ServerWorld serverworld, Biome biome) {
		BlockPos centerpos = new BlockPos(0, 0, 0);
		BlockPos biomepos = serverworld.func_241116_a_(biome, centerpos, 999999, 0);
		if (biomepos == null) {
			return null;
		}
		
		BlockPos spawnpos = null;
		for (int y = 255; y > 0; y--) {
			BlockPos checkpos = new BlockPos(biomepos.getX(), y, biomepos.getZ());
			if (serverworld.getBlockState(checkpos).getBlock().equals(Blocks.AIR)) {
				continue;
			}
			spawnpos = checkpos.up().toImmutable();
			break;
		}
		
		return spawnpos;
	}
	
	public static BlockPos getBlockPlayerIsLookingAt(World world, PlayerEntity player, boolean stopOnLiquid) {
        RayTraceResult raytraceresult = RayTraceFunctions.rayTrace(world, player, stopOnLiquid);
        double posX = raytraceresult.getHitVec().x;
        double posY = Math.floor(raytraceresult.getHitVec().y);
        double posZ = raytraceresult.getHitVec().z;

        return new BlockPos(posX, posY, posZ);
	}
	// END: GET functions
	
	
	// START: CHECK functions
	public static Boolean isOnSurface(World world, BlockPos pos) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int maxheight = world.getHeight();
		
		for (int y0 = y; y0 < maxheight; y0++) {
			BlockState blockstate = world.getBlockState(new BlockPos(x, y0, z));
			if (blockstate.getOpacity(world, pos) >= 15 && !(blockstate.getBlock() instanceof LeavesBlock)) {
				return false;
			}
		}
		
		return true;
	}
	public static Boolean isOnSurface(World world, Vector3d vecpos) {
		return isOnSurface(world, new BlockPos(vecpos.x, vecpos.y, vecpos.z));
	}
	
	
	public static Boolean withinDistance(BlockPos start, BlockPos end, int distance) {
		if (withinDistance(start, end, (double)distance)) {
			return true;
		}
		return false;
	}
	public static Boolean withinDistance(BlockPos start, BlockPos end, double distance) {
		if (start.withinDistance(end, distance)) {
			return true;
		}
		return false;
	}
	// END: CHECK functions
}