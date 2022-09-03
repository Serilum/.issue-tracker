/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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