/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.respawningshulkers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RespawningShulkersConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> timeInTicksToRespawn;
		public final ForgeConfigSpec.ConfigValue<Boolean> shulkersFromSpawnersDoNotRespawn;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			timeInTicksToRespawn = builder
					.comment("The amount of time in ticks it takes for a shulker to respawn after it died. 20 ticks = 1 second. By default 60 seconds.")
					.defineInRange("timeInTicksToRespawn", 1200, 1, 72000);
			shulkersFromSpawnersDoNotRespawn = builder
					.comment("If enabled, shulkers which spawn from (modded) spawners will not be respawned after a death.")
					.define("shulkersFromSpawnersDoNotRespawn", true);
			
			builder.pop();
		}
	}
}