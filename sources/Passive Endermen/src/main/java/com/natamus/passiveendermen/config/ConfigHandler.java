/*
 * This is the latest source code of Passive Endermen.
 * Minecraft version: 1.19.1, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.passiveendermen.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> preventEndermenFromTeleporting;
		public final ForgeConfigSpec.ConfigValue<Boolean> preventEndermenFromGriefing;
		public final ForgeConfigSpec.ConfigValue<Boolean> preventEndermenFromAttackingFirst;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			preventEndermenFromTeleporting = builder
					.comment("If enabled, prevents the endermen from teleporting.")
					.define("preventEndermenFromTeleporting", true);
			preventEndermenFromGriefing = builder
					.comment("If enabled, prevents from picking up and placing blocks.")
					.define("preventEndermenFromGriefing", true);
			preventEndermenFromAttackingFirst = builder
					.comment("If enabled, stops the endermen from attacking.")
					.define("preventEndermenFromAttackingFirst", true);
			
			builder.pop();
		}
	}
}