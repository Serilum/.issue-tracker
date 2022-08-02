/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.30.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective.functions;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.data.GlobalVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

		recursiveGetNextBlocks(world, startpos, startpos, possibleblocks, theblocksaround, checkedblocks, maxDistance);
		return theblocksaround;
	}
	private static void recursiveGetNextBlocks(Level world, BlockPos startpos, BlockPos pos, List<Block> possibleblocks, List<BlockPos> theblocksaround, List<BlockPos> checkedblocks, int maxDistance) {
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

		recursiveGetNextBlocksMaterial(world, startpos, startpos, possiblematerials, theblocksaround, checkedblocks, maxDistance);
		return theblocksaround;
	}
	private static void recursiveGetNextBlocksMaterial(Level world, BlockPos startpos, BlockPos pos, List<Material> possiblematerials, List<BlockPos> theblocksaround, List<BlockPos> checkedblocks, int maxDistance) {
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

	public static BlockPos getCenterNearbyVillage(ServerLevel serverworld) {
		return getNearbyVillage(serverworld, new BlockPos(0, 0, 0));
	}
	public static BlockPos getNearbyVillage(ServerLevel serverworld, BlockPos nearpos) {
		if (!WorldFunctions.isOverworld(serverworld)) {
			return null;
		}

		Registry<Structure> registry = serverworld.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);

		List<ResourceLocation> villagerls = new ArrayList<ResourceLocation>();
		for (ResourceLocation rl : registry.keySet()) {
			if (rl.toString().contains("village_")) {
				villagerls.add(rl);
			}
		}

		BlockPos closestvillage = null;
		for (int radius : Arrays.asList(200, 500, 1000)) {
			for (ResourceLocation rl : villagerls) {
				Optional<ResourceKey<Structure>> optional_vk = registry.getResourceKey(registry.get(rl));
				if (optional_vk.isPresent()) {
					ResourceKey<Structure> villagekey = optional_vk.get();
					Optional<Holder<Structure>> optional_s = registry.getHolder(villagekey);
					if (optional_s.isPresent()) {
						Holder<Structure> structure = optional_s.get();
						HolderSet<Structure> holderset = HolderSet.direct(structure);
						BlockPos villagepos = getNearbyStructure(serverworld, holderset, nearpos, radius);
						if (villagepos != null) {
							if (closestvillage != null) {
								if (nearpos.distManhattan(villagepos) >= nearpos.distManhattan(closestvillage)) {
									continue;
								}
							}

							closestvillage = villagepos.immutable();
						}
					}
				}

			}

			if (closestvillage != null) {
				break;
			}
		}

		return closestvillage;
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

	/*public static BlockPos getCenterBiome(ServerLevel serverworld, Predicate<Holder<Biome>> biome) {
		BlockPos centerpos = new BlockPos(0, 0, 0);
		BlockPos biomepos = serverworld.findNearestBiome(biome, centerpos, 999999, 0).getFirst();
		if (biomepos == null) {
			return null;
		}

		BlockPos spawnpos = null;
		for (int y = serverworld.getHeight()-1; y > 0; y--) {
			BlockPos checkpos = new BlockPos(biomepos.getX(), y, biomepos.getZ());
			if (serverworld.getBlockState(checkpos).getBlock().equals(Blocks.AIR)) {
				continue;
			}
			spawnpos = checkpos.above().immutable();
			break;
		}

		return spawnpos;
	}*/
	
	public static BlockPos getBlockPlayerIsLookingAt(Level world, Player player, boolean stopOnLiquid) {
		HitResult raytraceresult = RayTraceFunctions.rayTrace(world, player, stopOnLiquid);
        double posX = raytraceresult.getLocation().x;
        double posY = Math.floor(raytraceresult.getLocation().y);
        double posZ = raytraceresult.getLocation().z;

        return new BlockPos(posX, posY, posZ);
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