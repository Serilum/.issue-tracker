/*
 * This is the latest source code of Better Spawner Control.
 * Minecraft version: 1.19.3, mod version: 3.0.
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

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.TileEntityFunctions;
import com.natamus.collective.functions.WorldFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MobSpawnEvent {
	@SubscribeEvent
	public void onMobSpawn(LivingSpawnEvent.CheckSpawn e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}

		BaseSpawner msbl = e.getSpawner();
		if (msbl != null) {
			BlockEntity spawnerentity = msbl.getSpawnerBlockEntity();
			if (spawnerentity == null) {
				return;
			}

			BlockPos spawnerpos = spawnerentity.getBlockPos();
			
			boolean alltorches = true;
			for (BlockPos ap : BlockPosFunctions.getBlocksAround(spawnerpos, false)) {
				Block block = world.getBlockState(ap).getBlock();
				if (!(block instanceof TorchBlock) && !(block instanceof WallTorchBlock)) {
					alltorches = false;
					break;
				}
			}

			if (alltorches) {
				TileEntityFunctions.resetMobSpawnerDelay(msbl);
				e.setResult(Result.DENY);
			}
		}
	}
}
