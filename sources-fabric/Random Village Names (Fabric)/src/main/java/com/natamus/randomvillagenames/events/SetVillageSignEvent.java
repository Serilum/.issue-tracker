/*
 * This is the latest source code of Random Village Names.
 * Minecraft version: 1.19.2, mod version: 2.0.
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

package com.natamus.randomvillagenames.events;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.TileEntityFunctions;
import com.natamus.randomvillagenames.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SetVillageSignEvent {
	private static HashMap<ServerLevel, List<ChunkPos>> processChunks = new HashMap<ServerLevel, List<ChunkPos>>();

	private static HashMap<ServerLevel, CopyOnWriteArrayList<BlockPos>> existingvillages = new HashMap<ServerLevel, CopyOnWriteArrayList<BlockPos>>();
	private static HashMap<ServerLevel, ArrayList<ChunkPos>> cachedchunks = new HashMap<ServerLevel, ArrayList<ChunkPos>>();
	
	public static void onWorldLoad(ServerLevel serverlevel) {
		processChunks.put(serverlevel, new ArrayList<ChunkPos>());
		existingvillages.put(serverlevel, new CopyOnWriteArrayList<BlockPos>());
		cachedchunks.put(serverlevel, new ArrayList<ChunkPos>());
	}

	public static void onWorldTick(ServerLevel serverlevel) {
		if (processChunks.get(serverlevel).size() > 0) {
			ChunkPos chunkpos = processChunks.get(serverlevel).get(0);

			if (!cachedchunks.get(serverlevel).contains(chunkpos)) {
				cachedchunks.get(serverlevel).add(chunkpos);

				BlockPos worldpos = chunkpos.getWorldPosition();

				if (serverlevel.sectionsToVillage(SectionPos.of(worldpos)) <= 4) {
					for (BlockPos existingvillage : existingvillages.get(serverlevel)) {
						if (Math.abs(existingvillage.getX() - worldpos.getX()) <= 200) {
							if (Math.abs(existingvillage.getZ() - worldpos.getZ()) <= 200) {
								return;
							}
						}
					}

					BlockPos villagepos = BlockPosFunctions.getNearbyVillage(serverlevel, worldpos); // BlockPosFunctions.getNearbyStructure(serverworld, StructureFeature.VILLAGE, ppos, 100);
					if (villagepos == null) {
						return;
					}

					if (existingvillages.get(serverlevel).contains(villagepos)) {
						return;
					}
					existingvillages.get(serverlevel).add(villagepos);

					BlockPos twonorth = villagepos.immutable().north(2);
					if (Util.hasAreasSignNeaby(serverlevel, twonorth, 15)) {
						return;
					}

					BlockPos signpos = BlockPosFunctions.getSurfaceBlockPos(serverlevel, twonorth.getX(), twonorth.getZ());

					BlockState state = serverlevel.getBlockState(signpos);
					Block block = state.getBlock();
					while (!Util.isOverwritableBlockOrSign(block)) {
						signpos = signpos.above().immutable();
						if (signpos.getY() >= 256) {
							return;
						}

						state = serverlevel.getBlockState(signpos);
						block = state.getBlock();
					}

					try {
						Block northblock = serverlevel.getBlockState(signpos.north()).getBlock();
						if (!Util.isOverwritableBlockOrSign(northblock)) {
							serverlevel.setBlockAndUpdate(signpos, Blocks.OAK_WALL_SIGN.defaultBlockState().setValue(WallSignBlock.FACING, Direction.SOUTH));
						} else {
							serverlevel.setBlockAndUpdate(signpos, Blocks.OAK_SIGN.defaultBlockState());
						}
					} catch (ConcurrentModificationException ignored) {
					}

					BlockEntity te = serverlevel.getBlockEntity(signpos);
					if (!(te instanceof SignBlockEntity)) {
						return;
					}

					SignBlockEntity signentity = (SignBlockEntity) te;
					signentity.setMessage(0, Component.literal("[Area] 60"));
					TileEntityFunctions.updateTileEntity(serverlevel, signpos, signentity);
				}
			}

			processChunks.get(serverlevel).remove(0);
		}
	}

	public static void onChunkLoad(ServerLevel serverlevel, LevelChunk chunk) {
		if (!serverlevel.getServer().getWorldData().worldGenSettings().generateStructures()) {
			return;
		}

		ChunkPos chunkpos = chunk.getPos();

		if (cachedchunks.get(serverlevel).contains(chunkpos)) {
			return;
		}

		processChunks.get(serverlevel).add(chunkpos);
	}
}
