/*
 * This is the latest source code of Crying Ghasts.
 * Minecraft version: 1.19.3, mod version: 1.7.
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

package com.natamus.cryingghasts.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Integer> maxDistanceToGhastBlocks;
		public final ForgeConfigSpec.ConfigValue<Integer> ghastTearDelayTicks;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			maxDistanceToGhastBlocks = builder
					.comment("The maximum distance in blocks the player can be away from the ghasts in order for them to start dropping tears periodically.")
					.defineInRange("maxDistanceToGhastBlocks", 32, 1, 256);
			ghastTearDelayTicks = builder
					.comment("The delay in between ghasts dropping a tear in ticks (1 second = 20 ticks).")
					.defineInRange("ghastTearDelayTicks", 1200, 1, 72000);
			
			builder.pop();
		}
	}
}