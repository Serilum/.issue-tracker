/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.18.1, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Silence Mobs ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.silencemobs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> onlyAllowCommandWhenCheatsEnabled;
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldStick;
		public final ForgeConfigSpec.ConfigValue<Boolean> renameSilencedMobs;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			onlyAllowCommandWhenCheatsEnabled = builder
					.comment("If enabled, only allows the /silencestick command with cheats enabled.")
					.define("onlyAllowCommandWhenCheatsEnabled", true);
			mustHoldStick = builder
					.comment("If disabled, a stick will be generated via the /silencestick command instead of having to hold a stick while using the command.")
					.define("mustHoldStick", true);
			renameSilencedMobs = builder
					.comment("If enabled, whenever a player hits a non-silenced mob with The Silence Stick it will set their name to 'Silenced Entity'.")
					.define("renameSilencedMobs", true);
			
			builder.pop();
		}
	}
}