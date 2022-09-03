/*
 * This is the latest source code of Better Spawner Control.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.betterspawnercontrol.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.EntityFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;

public class MobSpawnEvent {
	final static List<Block> torchblocks = new ArrayList<Block>(Arrays.asList(Blocks.TORCH, Blocks.WALL_TORCH, Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH));
	
	public static boolean onMobSpawn(Mob entity, ServerLevel world, BlockPos spawnerPos, MobSpawnType spawnReason) {
		if (EntityFunctions.isEntityFromSpawner(entity)) {
			if (spawnerPos == null) {
				return true;
			}
			
			boolean alltorches = true;
			for (BlockPos ap : BlockPosFunctions.getBlocksAround(spawnerPos, false)) {
				Block block = world.getBlockState(ap).getBlock();
				if (!(block instanceof TorchBlock) && !(block instanceof WallTorchBlock)) {
					alltorches = false;
					break;
				}
			}
			
			if (alltorches) {
				return false;
			}
		}
		
		return true;
	}
}
