/*
 * This is the latest source code of Giant Spawn.
 * Minecraft version: 1.19.1, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.giantspawn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Double> chanceSurfaceZombieIsGiant;
		public final ForgeConfigSpec.ConfigValue<Boolean> shouldBurnGiantsInDaylight;
		public final ForgeConfigSpec.ConfigValue<Boolean> onlySpawnGiantOnSurface;
		
		public final ForgeConfigSpec.ConfigValue<Double> giantMovementSpeedModifier;
		public final ForgeConfigSpec.ConfigValue<Double> giantAttackDamageModifier;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			chanceSurfaceZombieIsGiant = builder
					.comment("The chance a zombie that has spawned on the surface is a giant.")
					.defineInRange("chanceSurfaceZombieIsGiant", 0.05, 0, 1.0);
			shouldBurnGiantsInDaylight = builder
					.comment("If enabled, burns giants when daylight shines upon them.")
					.define("shouldBurnGiantsInDaylight", true);
			onlySpawnGiantOnSurface = builder
					.comment("If enabled, a giant will only spawn on the surface.")
					.define("onlySpawnGiantOnSurface", true);
			
			giantMovementSpeedModifier = builder
					.comment("The giant movement speed modifier relative to the base zombie movement speed.")
					.defineInRange("giantMovementSpeedModifier", 1.0, 0, 20.0);
			giantAttackDamageModifier = builder
					.comment("The giant attack damage modifier relative to the base zombie attack damage.")
					.defineInRange("giantAttackDamageModifier", 2.0, 0, 20.0);
			
			
			
			builder.pop();
		}
	}
}