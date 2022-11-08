/*
 * This is the latest source code of Starter Kit.
 * Minecraft version: 1.19.2, mod version: 3.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.starterkit.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFTBIslandCreateCompatibility;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			enableFTBIslandCreateCompatibility = builder
					.comment("Whether the starter kit should be re-set after the '/ftbteamislands create' command from FTB Team Islands. Does nothing when it's not installed.")
					.define("enableFTBIslandCreateCompatibility", true);
			
			builder.pop();
		}
	}
}