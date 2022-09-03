/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.randomsheepcolours.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RandomSheepColoursConfigHandler {
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
