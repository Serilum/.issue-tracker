/*
 * This is the latest source code of Automatic Doors.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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