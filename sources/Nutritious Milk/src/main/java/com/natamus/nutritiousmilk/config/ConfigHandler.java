/*
 * This is the latest source code of Nutritious Milk.
 * Minecraft version: 1.18.1, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Nutritious Milk ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.nutritiousmilk.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Integer> hungerLevelIncrease;
		public final ForgeConfigSpec.ConfigValue<Double> saturationLevelIncrease;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			hungerLevelIncrease = builder
					.comment("The hunger level decreased. Example values: cookie = 2, bread = 5, salmon = 6, steak = 8.")
					.defineInRange("hungerLevelIncrease", 10, 0, 20);
			saturationLevelIncrease = builder
					.comment("The saturation increase. Example values: melon = 1.2, carrot = 3.6, chicken = 7.2, steak 12.8.")
					.defineInRange("saturationLevelIncrease", 10.0, 0, 20.0);
			
			builder.pop();
		}
	}
}