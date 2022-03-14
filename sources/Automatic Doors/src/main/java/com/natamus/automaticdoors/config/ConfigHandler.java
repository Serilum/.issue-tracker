/*
 * This is the latest source code of Automatic Doors.
 * Minecraft version: 1.18.2, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Automatic Doors ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.automaticdoors.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> doorOpenTime;
		public final ForgeConfigSpec.ConfigValue<Boolean> shouldOpenIronDoors;
		public final ForgeConfigSpec.ConfigValue<Boolean> preventOpeningOnSneak;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			doorOpenTime = builder
					.comment("The time in ms the door should stay open.")
					.defineInRange("doorOpenTime", 2500, 0, 10000);
			shouldOpenIronDoors = builder
					.comment("When enabled, iron doors will also be opened automatically.")
					.define("shouldOpenIronDoors", true);
			preventOpeningOnSneak = builder
					.comment("When enabled, doors won't be opened automatically when the player is sneaking.")
					.define("preventOpeningOnSneak", true);
			
			builder.pop();
		}
	}
}