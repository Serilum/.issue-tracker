/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.passiveendermen.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PassiveEndermenConfigHandler {
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
					.comment("If enabled, stops the endermen from attacking first when a player looks at them. Also works for endermite.")
					.define("preventEndermenFromAttackingFirst", true);
			
			builder.pop();
		}
	}
}