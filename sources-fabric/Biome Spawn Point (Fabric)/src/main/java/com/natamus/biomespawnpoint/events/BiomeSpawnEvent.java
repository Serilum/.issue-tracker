/*
 * This is the latest source code of Biome Spawn Point.
 * Minecraft version: 1.19.2, mod version: 1.6.
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

package com.natamus.biomespawnpoint.events;

import com.mojang.logging.LogUtils;
import com.natamus.biomespawnpoint.util.Util;
import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.FeatureFunctions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.ServerLevelData;
import org.slf4j.Logger;

public class BiomeSpawnEvent {
	private static final FabricLoader fabricLoader = FabricLoader.getInstance();
	private static final Logger logger = LogUtils.getLogger();

	public static void onWorldLoad(ServerLevel serverLevel, ServerLevelData serverLevelData) {
		BlockPos spawnPos = null;

		try {
			Registry<Biome> biomeRegistry = serverLevel.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
			Util.loadSpawnBiomeConfig(biomeRegistry);

			if (Util.spawnBiomeListSize() == 0) {
				logger.info("[Biome Spawn Point] No spawn biome specified in the spawnbiomes.txt config.");
			}
			else {
				String spawnBiome = Util.getSpawnBiome();
				if (spawnBiome.strip().equals("")) {
					logger.info("[Biome Spawn Point] Received spawn point biome name is empty.");
				}
				else {
					logger.info("[Biome Spawn Point] Finding the nearest '" + spawnBiome + "' biome. This might take a few seconds.");
					spawnPos = BlockPosFunctions.getCenterNearbyBiome(serverLevel, spawnBiome);
					if (spawnPos != null) {
						logger.info("[Biome Spawn Point] Biome found!");
					}
				}
			}
		}
		catch (Exception ex) {
			logger.info("[Biome Spawn Point] Unable to access Biome Registry on level load.");
		}

		WorldGenSettings generatorsettings = serverLevel.getServer().getWorldData().worldGenSettings();

		if (fabricLoader.isModLoaded("villagespawnpoint-fabric") && generatorsettings.generateStructures()) {
			if (spawnPos == null) {
				spawnPos = new BlockPos(0, 0, 0);
				logger.info("[Biome Spawn Point] Unable to find biome, but Village Spawn Point installed, finding village near x=0, z=0.");
			}
			else {
				logger.info("[Biome Spawn Point] Village Spawn Point installed, finding village near biome. This might take a few seconds.");
			}

			BlockPos villagePos = BlockPosFunctions.getNearbyVillage(serverLevel, spawnPos);
			if (villagePos != null) {
				logger.info("[Biome Spawn Point] Nearby village found.");
				spawnPos = villagePos.immutable();
			}
		}

		if (spawnPos == null) {
			logger.info("[Biome Spawn Point] Unable to find custom spawn point.");
			return;
		}

		logger.info("[Biome Spawn Point] The world will now generate.");

		serverLevel.setDefaultSpawnPos(spawnPos, 1.0f);

		if (generatorsettings.generateBonusChest()) {
			FeatureFunctions.placeBonusChest(serverLevel, spawnPos);
		}
	}
}
