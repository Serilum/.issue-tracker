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

package com.natamus.thevanillaexperience.mods.skeletonhorsespawn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SkeletonHorseSpawnConfigHandler {
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