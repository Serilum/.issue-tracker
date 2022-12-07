/*
 * This is the latest source code of Better Spawner Control.
 * Minecraft version: 1.19.3, mod version: 3.2.
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

package com.natamus.betterspawnercontrol.events;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.collective_fabric.functions.TileEntityFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

public class MobSpawnEvent {
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
				BlockEntity blockentity = world.getBlockEntity(spawnerPos);
				if (blockentity instanceof SpawnerBlockEntity) {
					SpawnerBlockEntity sbe = (SpawnerBlockEntity)blockentity;
					if (sbe != null) {
						BaseSpawner basespawner = sbe.getSpawner();
						TileEntityFunctions.resetMobSpawnerDelay(basespawner);
					}
				}
				return false;
			}
		}
		
		return true;
	}
}