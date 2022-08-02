/*
 * This is the latest source code of Difficulty Lock.
 * Minecraft version: 1.19.1, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.difficultylock.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.difficultylock.config.ConfigHandler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DifficultyLockEvent {
	@SubscribeEvent
	public void onWorldLoad(LevelEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		WorldData serverconfiguration = serverworld.getServer().getWorldData();
		
		LevelData worldinfo = world.getLevelData();
		boolean islocked = worldinfo.isDifficultyLocked();
		if (islocked && !ConfigHandler.GENERAL.shouldChangeDifficultyWhenAlreadyLocked.get()) {
			return;
		}
		
		Difficulty currentdifficulty = worldinfo.getDifficulty();
		if (ConfigHandler.GENERAL.forcePeaceful.get()) {
			if (!currentdifficulty.equals(Difficulty.PEACEFUL)) {
				//worldinfo.setDifficulty(Difficulty.PEACEFUL);
				serverconfiguration.setDifficulty(Difficulty.PEACEFUL);
			}
		}
		else if (ConfigHandler.GENERAL.forceEasy.get()) {
			if (!currentdifficulty.equals(Difficulty.EASY)) {
				//worldinfo.setDifficulty(Difficulty.EASY);
				serverconfiguration.setDifficulty(Difficulty.EASY);
			}			
		}
		else if (ConfigHandler.GENERAL.forceNormal.get()) {
			if (!currentdifficulty.equals(Difficulty.NORMAL)) {
				//worldinfo.setDifficulty(Difficulty.NORMAL);
				serverconfiguration.setDifficulty(Difficulty.NORMAL);
			}			
		}
		else if (ConfigHandler.GENERAL.forceHard.get()) {
			if (!currentdifficulty.equals(Difficulty.HARD)) {
				//worldinfo.setDifficulty(Difficulty.HARD);
				serverconfiguration.setDifficulty(Difficulty.HARD);
			}			
		}
		
		if (ConfigHandler.GENERAL.shouldLockDifficulty.get()) {
			if (!islocked) {
				//worldinfo.setDifficultyLocked(true);
				serverconfiguration.setDifficultyLocked(true);
			}
		}
	}
}
