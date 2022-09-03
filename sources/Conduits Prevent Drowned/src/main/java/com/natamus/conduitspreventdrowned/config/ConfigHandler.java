/*
 * This is the latest source code of Conduits Prevent Drowned.
 * Minecraft version: 1.19.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.conduitspreventdrowned.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> preventDrownedInRange;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			preventDrownedInRange = builder
					.comment("The euclidian distance range around the drowned where a check for a player with the conduit effect is done. A value of 400 prevents the spawning of all drowned around.")
					.defineInRange("preventDrownedInRange", 400, 0, 400);
			
			builder.pop();
		}
	}
}