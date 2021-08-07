/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
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

package com.natamus.thevanillaexperience.mods.mooshroomtweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MooshroomTweaksConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> becomeBrownChance;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			becomeBrownChance = builder
					.comment("The chance of a Red Mooshroom becoming a Brown Mooshroom.")
					.defineInRange("becomeBrownChance", 0.5, 0, 1.0);
			
			builder.pop();
		}
	}
}