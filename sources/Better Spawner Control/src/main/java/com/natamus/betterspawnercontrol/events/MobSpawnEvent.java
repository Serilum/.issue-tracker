/*
 * This is the latest source code of Better Spawner Control.
 * Minecraft version: 1.18.1, mod version: 1.5.
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
public class MobSpawnEvent {
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
