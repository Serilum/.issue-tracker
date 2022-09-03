/*
 * This is the latest source code of Random Shulker Colours.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.randomshulkercolours.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
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
