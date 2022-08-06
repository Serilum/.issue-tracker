/*
 * This is the latest source code of Spiders Produce Webs.
 * Minecraft version: 1.19.2, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.spidersproducewebs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Integer> maxDistanceToSpiderBlocks;
		public final ForgeConfigSpec.ConfigValue<Integer> spiderWebProduceDelayTicks;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			maxDistanceToSpiderBlocks = builder
					.comment("The maximum distance in blocks the player can be away from a spider in order for the it to produce a web periodically.")
					.defineInRange("maxDistanceToSpiderBlocks", 32, 1, 256);
			spiderWebProduceDelayTicks = builder
					.comment("The delay in between spiders producing a web in ticks (1 second = 20 ticks).")
					.defineInRange("spiderWebProduceDelayTicks", 12000, 1, 72000);
			
			builder.pop();
		}
	}
}