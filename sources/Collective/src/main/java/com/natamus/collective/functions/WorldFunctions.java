/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.41.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective.functions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelResource;

public class WorldFunctions {
	public static void setWorldTime(ServerLevel ServerLevel, Integer time) {
		if (time < 0 || time > 24000) {
			return;
		}

		int days = getTotalDaysPassed(ServerLevel);
		ServerLevel.setDayTime(time + (days*24000)); // setDayTime
	}
	
	public static int getTotalTimePassed(ServerLevel ServerLevel) {
		return (int)ServerLevel.getDayTime();
	}
	public static int getTotalDaysPassed(ServerLevel ServerLevel) {
		int currenttime = getTotalTimePassed(ServerLevel);
		return (int)Math.floor((double)currenttime/24000);
	}
	public static int getWorldTime(ServerLevel ServerLevel) {
		return getTotalTimePassed(ServerLevel) - (getTotalDaysPassed(ServerLevel)*24000);
	}
	
	// Dimension functions
	public static String getWorldDimensionName(Level world) {
		return world.dimension().location().toString();
	}
	public static boolean isOverworld(Level world) {
		return getWorldDimensionName(world).toLowerCase().endsWith("overworld");
	}
	public static boolean isNether(Level world) {
		return getWorldDimensionName(world).toLowerCase().endsWith("nether");
	}
	public static boolean isEnd(Level world) {
		return getWorldDimensionName(world).toLowerCase().endsWith("end");
	}
	
	// IWorld functions
	public static Level getWorldIfInstanceOfAndNotRemote(LevelAccessor iworld) {
		if (iworld.isClientSide()) {
			return null;
		}
		if (iworld instanceof Level) {
			return ((Level)iworld);
		}
		return null;
	}
	
	// Path
	public static String getWorldPath(ServerLevel ServerLevel) {
		String worldpath = ServerLevel.getServer().getWorldPath(LevelResource.ROOT).toString();
		return worldpath.substring(0, worldpath.length() - 2);
	}
}
