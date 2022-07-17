/*
 * This is the latest source code of Village Spawn Point.
 * Minecraft version: 1.19.0, mod version: 2.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Village Spawn Point ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillageSpawnEvent {
	@SubscribeEvent
	public void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		if (world instanceof ServerLevel == false) {
			return;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		WorldGenSettings generatorsettings = serverworld.getServer().getWorldData().worldGenSettings();
		
		if (!generatorsettings.generateStructures()) {
			return;
		}
		
		BlockPos spawnpos = BlockPosFunctions.getCenterNearbyVillage(serverworld);
		if (spawnpos == null) {
			return;
		}
		
		e.setCanceled(true);
		serverworld.setDefaultSpawnPos(spawnpos, 1.0f);
		
		if (generatorsettings.generateBonusChest()) {
			FeatureFunctions.placeBonusChest(serverworld, spawnpos);
		}
	}
}
