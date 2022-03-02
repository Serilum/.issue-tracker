/*
 * This is the latest source code of Bottled Air.
 * Minecraft version: 1.18.2, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Bottled Air ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.bottledair.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> holdFireTypeItemInOffhandToPreventWaterBottleCreation;
		public final ForgeConfigSpec.ConfigValue<Double> chanceGlassBottleBreaksWithFireTypeInOffhand;
		public final ForgeConfigSpec.ConfigValue<Integer> amountOfAirInBottles;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			holdFireTypeItemInOffhandToPreventWaterBottleCreation = builder
					.comment("Whether the creation of water bottles should be prevented (evaporated) when holding a fire type block in the offhand.")
					.define("holdFireTypeItemInOffhandToPreventWaterBottleCreation", true);
			chanceGlassBottleBreaksWithFireTypeInOffhand = builder
					.comment("The chance a glass bottle breaks when the item in the offhand evaporates the water, giving back an empty (air) bottle.")
					.defineInRange("chanceGlassBottleBreaksWithFireTypeInOffhand", 0.5, 0, 1.0);
			amountOfAirInBottles = builder
					.comment("The amount of air an empty bottle contains. In vanilla Minecraft, 300 is the maximum air supply.")
					.defineInRange("amountOfAirInBottles", 150, 0, 1000);
			
			builder.pop();
		}
	}
}