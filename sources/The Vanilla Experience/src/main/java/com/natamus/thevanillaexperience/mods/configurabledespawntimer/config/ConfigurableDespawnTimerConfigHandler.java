/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.configurabledespawntimer.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigurableDespawnTimerConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> despawnTimeInTicks;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			despawnTimeInTicks = builder
					.comment("The delay in ticks when an item should despawn, called the lifespan. Minecraft's default time is 6000 ticks. 1 second is 20 ticks.")
					.defineInRange("despawnTimeInTicks", 12000, 1, 2147483647);
			
			builder.pop();
		}
	}
}