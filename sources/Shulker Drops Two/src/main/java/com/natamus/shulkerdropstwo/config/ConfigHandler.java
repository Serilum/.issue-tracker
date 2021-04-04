/*
 * This is the latest source code of Shulker Drops Two.
 * Minecraft version: 1.16.5, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Shulker Drops Two ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.shulkerdropstwo.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> alwaysDropShells;
		public final ForgeConfigSpec.ConfigValue<Integer> shulkerDropAmount;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			alwaysDropShells = builder
					.comment("Ignore the drop chance (default 50%) that a Shulker will drop their shell and instead makes them always drop it.")
					.define("alwaysDropShells", false);
			shulkerDropAmount = builder
					.comment("Sets the amount of shells Shulkers drop.")
					.defineInRange("shulkerDropAmount", 2, 1, 64);
			
			builder.pop();
		}
	}
}