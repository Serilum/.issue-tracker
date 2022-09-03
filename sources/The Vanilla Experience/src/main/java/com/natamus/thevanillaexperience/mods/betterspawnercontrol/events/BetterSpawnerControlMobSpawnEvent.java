/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.betterspawnercontrol.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BetterSpawnerControlMobSpawnEvent {
	final static List<Block> torchblocks = new ArrayList<Block>(Arrays.asList(Blocks.TORCH, Blocks.WALL_TORCH, Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH));
	
	@SubscribeEvent
	public void onMobSpawn(LivingSpawnEvent.CheckSpawn e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}

		BaseSpawner msbl = e.getSpawner();
		if (msbl != null) {
			Entity tospawn = e.getEntity();
			BlockPos entitypos = tospawn.blockPosition();
			
			BlockPos spawnerpos = null;
			
			Map<BlockPos, BlockEntity> entities = world.getChunk(entitypos.getX() >> 4, entitypos.getZ() >> 4).getBlockEntities();
			for (BlockPos epos : entities.keySet()) {
				BlockEntity blockentity = entities.get(epos);
				if (blockentity.getType().equals(BlockEntityType.MOB_SPAWNER)) {
					if (epos.distSqr(entitypos, true) <= 50) {
						spawnerpos = epos.immutable();
						break;
					}
				}
			}
			
			if (spawnerpos == null) {
				return;
			}
			
			Boolean alltorches = true;
			for (BlockPos ap : BlockPosFunctions.getBlocksAround(spawnerpos, false)) {
				Block block = world.getBlockState(ap).getBlock();
				if (block instanceof TorchBlock == false && block instanceof WallTorchBlock == false) {
					alltorches = false;
					break;
				}
			}
			
			if (alltorches) {
				e.setResult(Result.DENY);
			}
		}
	}
}
