/*
 * This is the latest source code of Skeleton Horse Spawn.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.skeletonhorsespawn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Double> chanceSurfaceSkeletonHasHorse;
		public final ForgeConfigSpec.ConfigValue<Boolean> shouldBurnSkeletonHorsesInDaylight;
		public final ForgeConfigSpec.ConfigValue<Boolean> onlySpawnSkeletonHorsesOnSurface;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			chanceSurfaceSkeletonHasHorse = builder
					.comment("The chance a skeleton that has spawned on the surface is riding a horse.")
					.defineInRange("chanceSurfaceSkeletonHasHorse", 0.05, 0, 1.0);
			shouldBurnSkeletonHorsesInDaylight = builder
					.comment("If enabled, burns skeleton horses when daylight shines upon them.")
					.define("shouldBurnSkeletonHorsesInDaylight", true);
			onlySpawnSkeletonHorsesOnSurface = builder
					.comment("If enabled, a skeleton horse with rider will only spawn on the surface.")
					.define("onlySpawnSkeletonHorsesOnSurface", true);
			
			builder.pop();
		}
	}
}