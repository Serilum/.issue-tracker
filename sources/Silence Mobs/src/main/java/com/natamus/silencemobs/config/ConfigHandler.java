/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.19.2, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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