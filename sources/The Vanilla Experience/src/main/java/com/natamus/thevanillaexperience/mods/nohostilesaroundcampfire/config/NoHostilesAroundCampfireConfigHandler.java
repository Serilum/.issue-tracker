/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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