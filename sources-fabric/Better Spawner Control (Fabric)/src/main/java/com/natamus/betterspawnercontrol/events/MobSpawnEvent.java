/*
 * This is the latest source code of Better Spawner Control.
 * Minecraft version: 1.17.x, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Better Spawner Control ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
			
			Boolean alltorches = true;
			for (BlockPos ap : BlockPosFunctions.getBlocksAround(spawnerPos, false)) {
				Block block = world.getBlockState(ap).getBlock();
				if (block instanceof TorchBlock == false && block instanceof WallTorchBlock == false) {
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
