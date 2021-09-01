/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.1, mod version: 2.59.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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

		Integer days = getTotalDaysPassed(ServerLevel);
		ServerLevel.setDayTime(time + (days*24000)); // setDayTime
	}
	
	public static int getTotalTimePassed(ServerLevel ServerLevel) {
		return (int)ServerLevel.getDayTime();
	}
	public static int getTotalDaysPassed(ServerLevel ServerLevel) {
		Integer currenttime = getTotalTimePassed(ServerLevel);
		Integer days = (int)Math.floor((double)currenttime/24000);
		return days;
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
