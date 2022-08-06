/*
 * This is the latest source code of Move Boats.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.moveboats.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mustSneakToPickUp;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustSneakToPickUp = builder
					.comment("When enabled, will only pick up the boat when the player is sneaking and holding right-click.")
					.define("mustSneakToPickUp", true);
			
			builder.pop();
		}
	}
}