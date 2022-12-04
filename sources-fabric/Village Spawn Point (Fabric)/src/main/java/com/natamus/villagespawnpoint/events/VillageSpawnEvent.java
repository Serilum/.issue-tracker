/*
 * This is the latest source code of Village Spawn Point.
 * Minecraft version: 1.19.2, mod version: 3.0.
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

package com.natamus.villagespawnpoint.events;

import com.mojang.logging.LogUtils;
import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.FeatureFunctions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.ServerLevelData;
import org.slf4j.Logger;

public class VillageSpawnEvent {
	private static final FabricLoader fabricLoader = FabricLoader.getInstance();
	private static final Logger logger = LogUtils.getLogger();

	public static void onWorldLoad(ServerLevel serverworld, ServerLevelData serverLevelData) {
		if (fabricLoader.isModLoaded("biomespawnpoint-fabric")) {
			return;
		}

		WorldGenSettings generatorsettings = serverworld.getServer().getWorldData().worldGenSettings();
		
		if (!generatorsettings.generateStructures()) {
			return;
		}

		logger.info("[Village Spawn Point] Finding the nearest village. This might take a few seconds.");
		BlockPos spawnpos = BlockPosFunctions.getCenterNearbyVillage(serverworld);
		if (spawnpos == null) {
			return;
		}

		logger.info("[Village Spawn Point] Village found! The world will now generate.");
		
		serverworld.setDefaultSpawnPos(spawnpos, 1.0f);
		
		if (generatorsettings.generateBonusChest()) {
			FeatureFunctions.placeBonusChest(serverworld, spawnpos);
		}
	}
}
