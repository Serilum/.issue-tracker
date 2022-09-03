/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.enchantingcommands.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnchantingCommandsConfigHandler {
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