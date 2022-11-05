/*
 * This is the latest source code of Set World Spawn Point.
 * Minecraft version: 1.19.2, mod version: 2.7.
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

package com.natamus.setworldspawnpoint.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> _forceExactSpawn;
		public final ForgeConfigSpec.ConfigValue<Integer> xCoordSpawnPoint;
		public final ForgeConfigSpec.ConfigValue<Integer> yCoordSpawnPoint;
		public final ForgeConfigSpec.ConfigValue<Integer> zCoordSpawnPoint;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			_forceExactSpawn = builder
					.comment("If enabled, spawns players on the exact world spawn instead of around it.")
					.define("_forceExactSpawn", true);
			xCoordSpawnPoint = builder
					.comment("The X coordinate of the spawn point of newly created worlds.")
					.defineInRange("xCoordSpawnPoint", 0, -100000, 100000);
			yCoordSpawnPoint = builder
					.comment("The Y coordinate of the spawn point of newly created worlds. By default -1, which means it'll be the first solid block descending from y=256.")
					.defineInRange("yCoordSpawnPoint", -1, -1, 256);
			zCoordSpawnPoint = builder
					.comment("The Z coordinate of the spawn point of newly created worlds.")
					.defineInRange("zCoordSpawnPoint", 0, -100000, 100000);
			
			builder.pop();
		}
	}
}