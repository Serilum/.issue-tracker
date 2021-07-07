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

package com.natamus.thevanillaexperience.mods.doubledoors.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DoubleDoorsConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDoors;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFenceGates;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableTrapdoors;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			enableDoors = builder
					.comment("When enables, the mod works with double doors.")
					.define("enableDoors", true);
			enableFenceGates = builder
					.comment("When enables, the mod works with double fence gates.")
					.define("enableFenceGates", true);
			enableTrapdoors = builder
					.comment("When enables, the mod works with double trapdoors.")
					.define("enableTrapdoors", true);
			
			builder.pop();
		}
	}
}