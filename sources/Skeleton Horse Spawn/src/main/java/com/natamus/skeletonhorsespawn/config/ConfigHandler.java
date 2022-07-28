/*
 * This is the latest source code of Skeleton Horse Spawn.
 * Minecraft version: 1.19.1, mod version: 2.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Skeleton Horse Spawn ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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