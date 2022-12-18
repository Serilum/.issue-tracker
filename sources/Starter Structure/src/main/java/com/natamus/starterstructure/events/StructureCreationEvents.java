/*
 * This is the latest source code of Starter Structure.
 * Minecraft version: 1.19.3, mod version: 1.2.
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

package com.natamus.starterstructure.events;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.FeatureFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.starterstructure.config.ConfigHandler;
import com.natamus.starterstructure.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class StructureCreationEvents {
	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOWEST)
	public void onLevelSpawn(LevelEvent.CreateSpawnPosition e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}
		
		if (!(level instanceof ServerLevel)) {
			return;
		}

		ServerLevel serverLevel = (ServerLevel)level;
		WorldOptions generatorsettings = serverLevel.getServer().getWorldData().worldGenOptions();

		if (ConfigHandler.GENERAL.shouldSetSpawnPoint.get()) {
			int x = ConfigHandler.GENERAL.spawnXCoordinate.get();
			int y = ConfigHandler.GENERAL.spawnYCoordinate.get();
			int z = ConfigHandler.GENERAL.spawnZCoordinate.get();

			if (y < 0) {
				y = BlockPosFunctions.getSurfaceBlockPos(serverLevel, x, z).getY();
			}
			else if (y > serverLevel.getMaxBuildHeight()) {
				y = serverLevel.getMaxBuildHeight() - 1;
			}

			BlockPos spawnPos = new BlockPos(x, y, z);
			serverLevel.setDefaultSpawnPos(spawnPos, 1.0F);

			if (generatorsettings.generateBonusChest()) {
				FeatureFunctions.placeBonusChest(serverLevel, spawnPos);
			}
		}

		serverLevel.getServer().tell(new TickTask(serverLevel.getServer().getTickCount()+1, () -> {
			if (ConfigHandler.GENERAL.shouldGenerateStructure.get()) {
				Util.generateSchematic(serverLevel);
			}
		}));
	}

	@SubscribeEvent
	public void onLevelLoad(LevelEvent.Load e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		Util.readProtectedList((ServerLevel)level);
	}
}
