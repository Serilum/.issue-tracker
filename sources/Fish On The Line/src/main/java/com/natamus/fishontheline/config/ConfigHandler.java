/*
 * This is the latest source code of Fish On The Line.
 * Minecraft version: 1.19.1, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.fishontheline.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldBellInOffhand;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHoldBellInOffhand = builder
					.comment("If enabled, the fish on the line sound will only go off if a bell is held in the offhand while fishing.")
					.define("mustHoldBellInOffhand", true);
			
			builder.pop();
		}
	}
}