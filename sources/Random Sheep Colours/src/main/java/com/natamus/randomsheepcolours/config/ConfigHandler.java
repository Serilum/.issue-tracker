/*
 * This is the latest source code of Random Sheep Colours.
 * Minecraft version: 1.19.1, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.randomsheepcolours.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<String> possibleSheepColours;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			possibleSheepColours = builder
					.comment("The possible sheep colours which the mod chooses from, divided by a comma.")
					.define("possibleSheepColours", "black,blue,brown,cyan,gray,green,jeb,light_blue,light_gray,lime,magenta,orange,pink,purple,red,white,yellow");
			
			builder.pop();
		}
	}
}
