/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.16.5, mod version: 2.27.
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

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;

public class WorldFunctions {
	public static void setWorldTime(ServerWorld serverworld, Integer time) {
		if (time < 0 || time > 24000) {
			return;
		}

		Integer days = getTotalDaysPassed(serverworld);
		serverworld.setDayTime(time + (days*24000)); // setDayTime
	}
	
	public static int getTotalTimePassed(ServerWorld serverworld) {
		return (int)serverworld.getDayTime();
	}
	public static int getTotalDaysPassed(ServerWorld serverworld) {
		Integer currenttime = getTotalTimePassed(serverworld);
		Integer days = (int)Math.floor((double)currenttime/24000);
		return days;
	}
	public static int getWorldTime(ServerWorld serverworld) {
		return getTotalTimePassed(serverworld) - (getTotalDaysPassed(serverworld)*24000);
	}
	
	// Dimension functions
	public static String getWorldDimensionName(World world) {
		return world.dimension().location().toString();
	}
	public static boolean isOverworld(World world) {
		return getWorldDimensionName(world).toLowerCase().endsWith("overworld");
	}
	public static boolean isNether(World world) {
		return getWorldDimensionName(world).toLowerCase().endsWith("nether");
	}
	public static boolean isEnd(World world) {
		return getWorldDimensionName(world).toLowerCase().endsWith("end");
	}
	
	// IWorld functions
	public static World getWorldIfInstanceOfAndNotRemote(IWorld iworld) {
		if (iworld.isClientSide()) {
			return null;
		}
		if (iworld instanceof World) {
			return ((World)iworld);
		}
		return null;
	}
	
	// Path
	public static String getWorldPath(ServerWorld serverworld) {
		String worldpath = serverworld.getServer().getWorldPath(FolderName.ROOT).toString();
		return worldpath.substring(0, worldpath.length() - 2);
	}
}
