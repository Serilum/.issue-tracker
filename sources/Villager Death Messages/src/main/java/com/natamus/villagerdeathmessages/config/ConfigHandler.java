/*
 * This is the latest source code of Villager Death Messages.
 * Minecraft version: 1.19.2, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.villagerdeathmessages.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> showLocation;
		public final ForgeConfigSpec.ConfigValue<Boolean> mentionModdedVillagers;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			showLocation = builder
					.comment("If enabled, shows the location of the villager in the death message.")
					.define("showLocation", true);
			mentionModdedVillagers = builder
					.comment("If enabled, also shows death messages of modded villagers. If you've found a 'villager'-entity that isn't named let me know by opening an issue so I can add it in.")
					.define("mentionModdedVillagers", true);
			
			builder.pop();
		}
	}
}