/*
 * This is the latest source code of Enchanting Commands.
 * Minecraft version: 1.19.1, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.enchantingcommands.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<String> enchantCommandString;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			enchantCommandString = builder
					.comment("The default command to use the features of this mod. By default /ec")
					.define("enchantCommandString", "ec");
			
			builder.pop();
		}
	}
}