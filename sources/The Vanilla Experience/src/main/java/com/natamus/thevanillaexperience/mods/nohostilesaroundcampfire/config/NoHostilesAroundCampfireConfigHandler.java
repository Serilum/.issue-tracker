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

package com.natamus.thevanillaexperience.mods.nohostilesaroundcampfire.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NoHostilesAroundCampfireConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> preventHostilesRadius;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> burnHostilesAroundWhenPlaced;
		public final ForgeConfigSpec.ConfigValue<Double> burnHostilesRadiusModifier;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> preventMobSpawnerSpawns;
		public final ForgeConfigSpec.ConfigValue<Boolean> campfireMustBeLit;
		public final ForgeConfigSpec.ConfigValue<Boolean> campfireMustBeSignalling;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> enableEffectForNormalCampfires;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableEffectForSoulCampfires;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			preventHostilesRadius = builder
					.comment("The radius around the campfire in blocks where hostile mob spawns will be blocked.")
					.defineInRange("preventHostilesRadius", 48, 1, 128);
			
			burnHostilesAroundWhenPlaced = builder
					.comment("If enabled, burns all hostile mobs around the campfire within the radius whenever a player places a campfire.")
					.define("burnHostilesAroundWhenPlaced", true);
			burnHostilesRadiusModifier = builder
					.comment("By default set to 0.5. This means that if the radius is 16, the campfire burns prior mobs in a radius of 8.")
					.defineInRange("burnHostilesRadiusModifier", 0.5, 0, 1.0);
			
			preventMobSpawnerSpawns = builder
					.comment("When enabled, the mob spawners spawns are also disabled when a campfire is within the radius.")
					.define("preventMobSpawnerSpawns", false);
			campfireMustBeLit = builder
					.comment("When enabled, the campfire only has an effect when the block is lit up.")
					.define("campfireMustBeLit", true);
			campfireMustBeSignalling = builder
					.comment("When enabled, the campfire only has an effect when the block is signalling.")
					.define("campfireMustBeSignalling", false);
			
			enableEffectForNormalCampfires = builder
					.comment("When enabled, the mod will work with normal campfires.")
					.define("enableEffectForNormalCampfires", true);
			enableEffectForSoulCampfires = builder
					.comment("When enabled, the mod will work with soul campfires.")
					.define("enableEffectForSoulCampfires", true);
			
			builder.pop();
		}
	}
}