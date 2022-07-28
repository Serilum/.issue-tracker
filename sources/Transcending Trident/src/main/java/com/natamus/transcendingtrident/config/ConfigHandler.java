/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.19.1, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Transcending Trident ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.transcendingtrident.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldBucketOfWater;
		public final ForgeConfigSpec.ConfigValue<Integer> tridentUseDuration;
		public final ForgeConfigSpec.ConfigValue<Double> tridentUsePowerModifier;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHoldBucketOfWater = builder
					.comment("When enabled, Riptide can only be used without rain when the user is holding a bucket of water.")
					.define("mustHoldBucketOfWater", true);
			tridentUseDuration = builder
					.comment("The amount of time a player needs to charge the trident before being able to use Riptide. Minecraft's default is 10.")
					.defineInRange("tridentUseDuration", 5, 0, 20);
			tridentUsePowerModifier = builder
					.comment("The riptide power of the trident is multiplied by this number on use. Allows traveling a greater distance with a single charge.")
					.defineInRange("tridentUsePowerModifier", 3.0, 0, 100.0);
			
			builder.pop();
		}
	}
}