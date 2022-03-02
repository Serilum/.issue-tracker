/*
 * This is the latest source code of Difficulty Lock.
 * Minecraft version: 1.19.x, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Difficulty Lock ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.difficultylock.events;

import com.natamus.difficultylock.config.ConfigHandler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.WorldData;

public class DifficultyLockEvent {
	public static void onWorldLoad(ServerLevel world) {
		WorldData serverconfiguration = world.getServer().getWorldData();
		
		LevelData worldinfo = world.getLevelData();
		boolean islocked = worldinfo.isDifficultyLocked();
		if (islocked && !ConfigHandler.shouldChangeDifficultyWhenAlreadyLocked.getValue()) {
			return;
		}
		
		Difficulty currentdifficulty = worldinfo.getDifficulty();
		if (ConfigHandler.forcePeaceful.getValue()) {
			if (!currentdifficulty.equals(Difficulty.PEACEFUL)) {
				serverconfiguration.setDifficulty(Difficulty.PEACEFUL);
			}
		}
		else if (ConfigHandler.forceEasy.getValue()) {
			if (!currentdifficulty.equals(Difficulty.EASY)) {
				serverconfiguration.setDifficulty(Difficulty.EASY);
			}			
		}
		else if (ConfigHandler.forceNormal.getValue()) {
			if (!currentdifficulty.equals(Difficulty.NORMAL)) {
				serverconfiguration.setDifficulty(Difficulty.NORMAL);
			}			
		}
		else if (ConfigHandler.forceHard.getValue()) {
			if (!currentdifficulty.equals(Difficulty.HARD)) {
				serverconfiguration.setDifficulty(Difficulty.HARD);
			}			
		}
		
		if (ConfigHandler.shouldLockDifficulty.getValue()) {
			if (!islocked) {
				serverconfiguration.setDifficultyLocked(true);
			}
		}
	}
}
