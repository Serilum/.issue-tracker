/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.randomshulkercolours.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RandomShulkerColoursConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<String> possibleShulkerColours;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			possibleShulkerColours = builder
					.comment("The possible shulker colours which the mod chooses from, divided by a comma.")
					.define("possibleShulkerColours", "normal,black,blue,brown,cyan,gray,green,light_blue,light_gray,lime,magenta,orange,pink,purple,red,white,yellow");
			
			builder.pop();
		}
	}
}
