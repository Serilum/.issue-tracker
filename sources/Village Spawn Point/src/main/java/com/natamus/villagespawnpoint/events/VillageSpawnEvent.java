/*
 * This is the latest source code of Village Spawn Point.
 * Minecraft version: 1.19.2, mod version: 2.9.
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

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.FeatureFunctions;
import com.natamus.collective.functions.WorldFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillageSpawnEvent {
	@SubscribeEvent(receiveCanceled = true)
	public void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
		if (ModList.get().isLoaded("biomespawnpoint")) {
			return;
		}

		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		if (!(world instanceof ServerLevel)) {
			return;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		WorldGenSettings generatorsettings = serverworld.getServer().getWorldData().worldGenSettings();
		
		if (!generatorsettings.generateStructures()) {
			return;
		}

		System.out.println("[Village Spawn Point] Finding the nearest village. This might take a few seconds.");
		BlockPos spawnpos = BlockPosFunctions.getCenterNearbyVillage(serverworld);
		if (spawnpos == null) {
			return;
		}

		System.out.println("[Village Spawn Point] Village found! The world will now generate.");
		
		e.setCanceled(true);
		serverworld.setDefaultSpawnPos(spawnpos, 1.0f);
		
		if (generatorsettings.generateBonusChest()) {
			FeatureFunctions.placeBonusChest(serverworld, spawnpos);
		}
	}
}
