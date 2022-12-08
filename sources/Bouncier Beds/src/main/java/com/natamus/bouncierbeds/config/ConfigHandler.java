/*
 * This is the latest source code of Bouncier Beds.
 * Minecraft version: 1.19.3, mod version: 1.3.
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

package com.natamus.bouncierbeds.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> bedsPreventFallDamage;
		public final ForgeConfigSpec.ConfigValue<Double> bedBounciness;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			bedsPreventFallDamage = builder
					.comment("Whether beds should prevent fall damage when a player lands on them. It's recommended to keep this enabled if you have lots of bounciness.")
					.define("bedsPreventFallDamage", true);
			bedBounciness = builder
					.comment("The modifier of how much a bed bounces. A value of 2.0 makes the player jump around 30 blocks. A value of 100.0 makes the player jump around 4500 blocks.")
					.defineInRange("bedBounciness", 1.5, 0.0, 100.0);
			
			builder.pop();
		}
	}
}