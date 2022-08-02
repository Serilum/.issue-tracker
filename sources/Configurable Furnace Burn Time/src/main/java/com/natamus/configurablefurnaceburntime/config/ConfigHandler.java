/*
 * This is the latest source code of Configurable Furnace Burn Time.
 * Minecraft version: 1.19.1, mod version: 1.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.configurablefurnaceburntime.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> burnTimeModifier;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			burnTimeModifier = builder
					.comment("How much the fuel burn time should be modified. It's calculated by default burn time * burnTimeModifier. For example: sticks are by default 100 ticks (5 seconds). A burnTimeModifier of 2.0 makes it 200 ticks (10 seconds).")
					.defineInRange("burnTimeModifier", 1.0, 0.0, 1000.0);
			
			builder.pop();
		}
	}
}