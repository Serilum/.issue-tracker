/*
 * This is the latest source code of Sleep Sooner.
 * Minecraft version: 1.19.0, mod version: 2.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Sleep Sooner ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.sleepsooner.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> enableSleepSooner;
		public final ForgeConfigSpec.ConfigValue<Boolean> enablePreSleepMessage;
		public final ForgeConfigSpec.ConfigValue<Integer> whenSleepIsPossibleInTicks;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			enableSleepSooner = builder
					.comment("Allows you to disable/enable the Sleep Sooner mod.")
					.define("_enableSleepSooner", true);
			enablePreSleepMessage = builder
					.comment("Allows you to disable/enable the message you receive before sleeping. You still need to click the bed twice if the current time is below 12540.")
					.define("enablePreSleepMessage", true);
			whenSleepIsPossibleInTicks = builder
					.comment("The default time in ticks when you can sleep is ~12540. The default Sleep Sooner mod value is 12000.")
					.defineInRange("whenSleepIsPossibleInTicks", 12000, 0, 24000);
			
			builder.pop();
		}
	}
}