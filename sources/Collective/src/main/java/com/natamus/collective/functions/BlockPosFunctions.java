/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.17.
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

package com.natamus.collective.functions;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.data.GlobalVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BlockPosFunctions {
	// START: GET functions
	public static List<BlockPos> getBlocksAround(BlockPos pos, boolean down) {
		List<BlockPos> around = new ArrayList<BlockPos>();
		around.add(pos.north());
		around.add(pos.east());
		around.add(pos.south());
		around.add(pos.west());
		around.add(pos.above());
		if (down) {
			around.add(pos.below());
		}
		return around;
	}

	// START: RECURSIVE GET BLOCKS
	private static HashMap<BlockPos, Integer> rgnbcount = new HashMap<BlockPos, Integer>();

	public static List<BlockPos> getBlocksNextToEachOther(Level world, BlockPos startpos, List<Block> possibleblocks) {
		return getBlocksNextToEachOther(world, startpos, possibleblocks, 50);
	}
	public static List<BlockPos> getBlocksNextToEachOther(Level world, BlockPos startpos, List<Block> possibleblocks, int maxDistance) {
		List<BlockPos> checkedblocks = new ArrayList<BlockPos>();
		List<BlockPos> theblocksaround = new ArrayList<BlockPos>();
		if (possibleblocks.contains(world.getBlockState(startpos).getBlock())) {
			theblocksaround.add(startpos);
			checkedblocks.add(startpos);
		}

		rgnbcount.put(startpos.immutable(), 0);
		recursiveGetNextBlocks(world, startpos, startpos, possibleblocks, theblocksaround, checkedblocks, maxDistance);
		return theblocksaround;
	}
	private static void recursiveGetNextBlocks(Level world, BlockPos startpos, BlockPos pos, List<Block> possibleblocks, List<BlockPos> theblocksaround, List<BlockPos> checkedblocks, int maxDistance) {
		int rgnbc = rgnbcount.get(startpos);
		if (rgnbc > 100) {
			return;
		}
		rgnbcount.put(startpos, rgnbc + 1);

		List<BlockPos> possibleblocksaround = getBlocksAround(pos, true);
		for (BlockPos pba : possibleblocksaround) {
			if (checkedblocks.contains(pba)) {
				continue;
			}
			checkedblocks.add(pba);

			if (possibleblocks.contains(world.getBlockState(pba).getBlock())) {
				if (!theblocksaround.contains(pba)) {
					theblocksaround.add(pba);
					if (BlockPosFunctions.withinDistance(startpos, pba, maxDistance)) {
						recursiveGetNextBlocks(world, startpos, pba, possibleblocks, theblocksaround, checkedblocks, maxDistance);
					}
				}
			}
		}
	}
	private static HashMap<BlockPos, Integer> rgnbmcount = new HashMap<BlockPos, Integer>();

	public static List<BlockPos> getBlocksNextToEachOtherMaterial(Level world, BlockPos startpos, List<Material> possiblematerials) {
		return getBlocksNextToEachOtherMaterial(world, startpos, possiblematerials, 50);
	}
	public static List<BlockPos> getBlocksNextToEachOtherMaterial(Level world, BlockPos startpos, List<Material> possiblematerials, int maxDistance) {
		List<BlockPos> checkedblocks = new ArrayList<BlockPos>();
		List<BlockPos> theblocksaround = new ArrayList<BlockPos>();
		if (possiblematerials.contains(world.getBlockState(startpos).getMaterial())) {
			theblocksaround.add(startpos);
			checkedblocks.add(startpos);
		}

		rgnbmcount.put(startpos.immutable(), 0);
		recursiveGetNextBlocksMaterial(world, startpos, startpos, possiblematerials, theblocksaround, checkedblocks, maxDistance);
		return theblocksaround;
	}
	private static void recursiveGetNextBlocksMaterial(Level world, BlockPos startpos, BlockPos pos, List<Material> possiblematerials, List<BlockPos> theblocksaround, List<BlockPos> checkedblocks, int maxDistance) {
		int rgnbmc = rgnbmcount.get(startpos);
		if (rgnbmc > 100) {
			return;
		}
		rgnbmcount.put(startpos, rgnbmc + 1);

		List<BlockPos> possibleblocksaround = getBlocksAround(pos, true);
		for (BlockPos pba : possibleblocksaround) {
			if (checkedblocks.contains(pba)) {
				continue;
			}
			checkedblocks.add(pba);

			if (possiblematerials.contains(world.getBlockState(pba).getMaterial())) {
				if (!theblocksaround.contains(pba)) {
					theblocksaround.add(pba);
					if (BlockPosFunctions.withinDistance(startpos, pba, maxDistance)) {
						recursiveGetNextBlocksMaterial(world, startpos, pba, possiblematerials, theblocksaround, checkedblocks, maxDistance);
					}
				}
			}
		}
	}
	// END RECURSIVE GET BLOCKS
	
	public static BlockPos getSurfaceBlockPos(ServerLevel serverworld, int x, int z) {
		int height = serverworld.getHeight();
		
		BlockPos returnpos = new BlockPos(x, height-1, z);
		if (!WorldFunctions.isNether(serverworld)) {
			BlockPos pos = new BlockPos(x, height, z);
			for (int y = height; y > 0; y--) {
				BlockState blockstate = serverworld.getBlockState(pos);
				Material material = blockstate.getMaterial();
				if (blockstate.getLightBlock(serverworld, pos) >= 15 || GlobalVariables.surfacematerials.contains(material)) {
					returnpos = pos.above().immutable();
					break;
				}
				
				pos = pos.below();
			}
		}
		else {
			int maxheight = 128;
			BlockPos pos = new BlockPos(x, 0, z);
			for (int y = 0; y < maxheight; y++) {
				BlockState blockstate = serverworld.getBlockState(pos);
				if (blockstate.getBlock().equals(Blocks.AIR)) {
					BlockState upstate = serverworld.getBlockState(pos.above());
					if (upstate.getBlock().equals(Blocks.AIR)) {
						returnpos = pos.immutable();
						break;
					}
				}
				
				pos = pos.above();
			}
		}
		
		return returnpos;
	}

	public static BlockPos getCenterNearbyVillage(ServerLevel serverLevel) {
		return getNearbyVillage(serverLevel, new BlockPos(0, 0, 0));
	}
	public static BlockPos getNearbyVillage(ServerLevel serverLevel, BlockPos nearPos) {
		BlockPos closestvillage = null;
		if (!serverLevel.getServer().getWorldData().worldGenSettings().generateStructures()) {
			return null;
		}

		String rawOutput = CommandFunctions.getRawCommandOutput(serverLevel, Vec3.atBottomCenterOf(nearPos), "/locate structure #minecraft:village");

		if (rawOutput.contains("nearest") && rawOutput.contains("[")) {
			String rawcoords = rawOutput.split("nearest")[1].split("\\[")[1].split("\\]")[0];
			String[] coords = rawcoords.split(", ");
			if (coords.length == 3) {
				String sx = coords[0];
				String sz = coords[2];
				if (NumberFunctions.isNumeric(sx) && NumberFunctions.isNumeric(sz)) {
					return getSurfaceBlockPos(serverLevel, Integer.parseInt(sx), Integer.parseInt(sz));
				}
			}
		}

		return closestvillage;
	}

	public static BlockPos getCenterNearbyBiome(ServerLevel serverLevel, String biome) {
		return getNearbyBiome(serverLevel, new BlockPos(0, 0, 0), biome);
	}
	public static BlockPos getNearbyBiome(ServerLevel serverLevel, BlockPos nearPos, String biome) {
		String rawOutput = CommandFunctions.getRawCommandOutput(serverLevel, Vec3.atBottomCenterOf(nearPos), "/locate biome " + biome);

		if (rawOutput.contains("nearest") && rawOutput.contains("[")) {
			String rawcoords = rawOutput.split("nearest")[1].split("\\[")[1].split("\\]")[0];
			String[] coords = rawcoords.split(", ");
			if (coords.length == 3) {
				String sx = coords[0];
				String sz = coords[2];
				if (NumberFunctions.isNumeric(sx) && NumberFunctions.isNumeric(sz)) {
					return getSurfaceBlockPos(serverLevel, Integer.parseInt(sx), Integer.parseInt(sz));
				}
			}
		}

		return null;
	}

	public static BlockPos getCenterNearbyStructure(ServerLevel serverworld, HolderSet<Structure> structure) {
		return getNearbyStructure(serverworld, structure, new BlockPos(0, 0, 0));
	}
	public static BlockPos getNearbyStructure(ServerLevel serverworld, HolderSet<Structure> structure, BlockPos nearpos) {
		return getNearbyStructure(serverworld, structure, nearpos, 9999);
	}
	public static BlockPos getNearbyStructure(ServerLevel serverworld, HolderSet<Structure> structure, BlockPos nearpos, int radius) {
		Pair<BlockPos, Holder<Structure>> pair = serverworld.getChunkSource().getGenerator().findNearestMapStructure(serverworld, structure, nearpos, radius, false);
		if (pair == null) {
			return null;
		}

		BlockPos villagepos = pair.getFirst();
		if (villagepos == null) {
			return null;
		}

		BlockPos spawnpos = null;
		for (int y = serverworld.getHeight()-1; y > 0; y--) {
			BlockPos checkpos = new BlockPos(villagepos.getX(), y, villagepos.getZ());
			if (serverworld.getBlockState(checkpos).getBlock().equals(Blocks.AIR)) {
				continue;
			}
			spawnpos = checkpos.above().immutable();
			break;
		}

		return spawnpos;
	}
	
	public static BlockPos getBlockPlayerIsLookingAt(Level world, Player player, boolean stopOnLiquid) {
		HitResult raytraceresult = RayTraceFunctions.rayTrace(world, player, stopOnLiquid);
        double posX = raytraceresult.getLocation().x;
        double posY = Math.floor(raytraceresult.getLocation().y);
        double posZ = raytraceresult.getLocation().z;

        return new BlockPos(posX, posY, posZ);
	}

	public static BlockPos getRandomCoordinatesInNearestUngeneratedChunk(ServerLevel serverLevel, BlockPos aroundPosition) {
		List<String> regionList = new ArrayList<String>();

		try {
			File regionFolder = new File(WorldFunctions.getWorldPath(serverLevel) + File.separator + "region");
			File[] listOfRegionFiles = regionFolder.listFiles();

			for (File regionFile : listOfRegionFiles) {
				if (regionFile.isFile()) {
					regionList.add(regionFile.getName().replaceAll("r.", "").replaceAll(".mca", ""));
				}
			}
		}
		catch(NullPointerException ignored) {
			return null;
		}

		ChunkPos chunkPos = serverLevel.getChunkAt(aroundPosition).getPos();
		int curRegionX = chunkPos.getRegionX();
		int curRegionZ = chunkPos.getRegionZ();

		int currentRange = 1;
		int loops = 0;

		String closestUngeneratedRegionString = "";
		while (closestUngeneratedRegionString.equals("")) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					int regionX = curRegionX + (x*currentRange);
					int regionZ = curRegionZ + (z*currentRange);

					String regionString = regionX + "." + regionZ;
					if (!regionList.contains(regionString)) {
						closestUngeneratedRegionString = regionString;
						break;
					}
				}
				if (!closestUngeneratedRegionString.equals("")) {
					break;
				}
			}

			currentRange += 1;
			loops += 1;

			if (loops > 50) {
				return null;
			}
		}

		int outputRegionX, outputRegionZ;

		String[] cursspl = closestUngeneratedRegionString.split("\\.");
		try {
			outputRegionX = Integer.parseInt(cursspl[0]);
			outputRegionZ = Integer.parseInt(cursspl[1]);
		}
		catch (NumberFormatException ex) {
			return null;
		}

		int minXRange = (outputRegionX * 512) - 256;
		int maxXRange = (outputRegionX * 512) + 256;
		int minZRange = (outputRegionZ * 512) - 256;
		int maxZRange = (outputRegionZ * 512) + 256;

		int randomXCoord = ThreadLocalRandom.current().nextInt(minXRange, maxXRange + 1);
		int randomZCoord = ThreadLocalRandom.current().nextInt(minZRange, maxZRange + 1);
		int randomYCoord = BlockPosFunctions.getSurfaceBlockPos(serverLevel, randomXCoord, randomZCoord).getY();

		return new BlockPos(randomXCoord, randomYCoord, randomZCoord);
	}
	// END: GET functions
	
	
	// START: CHECK functions
	public static Boolean isOnSurface(Level world, BlockPos pos) {
		return world.canSeeSky(pos);
	}
	public static Boolean isOnSurface(Level world, Vec3 vecpos) {
		return isOnSurface(world, new BlockPos(vecpos.x, vecpos.y, vecpos.z));
	}
	
	
	public static Boolean withinDistance(BlockPos start, BlockPos end, int distance) {
		return withinDistance(start, end, (double) distance);
	}
	public static Boolean withinDistance(BlockPos start, BlockPos end, double distance) {
		return start.closerThan(end, distance);
	}
	// END: CHECK functions
}