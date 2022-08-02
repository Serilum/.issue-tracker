/*
 * This is the latest source code of Name Tag Tweaks.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.nametagtweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> droppedNameTagbyEntityKeepsNameValue;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNameTagCommand;
		public final ForgeConfigSpec.ConfigValue<Boolean> nameTagCommandReplaceUnderscoresWithSpaces;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			droppedNameTagbyEntityKeepsNameValue = builder
					.comment("If enabled, any name tag that drops on an entity's death will keep its named value.")
					.define("droppedNameTagbyEntityKeepsNameValue", true);
			
			enableNameTagCommand = builder
					.comment("If enabled, adds the /nametag <name> command. Used to change the name value without an anvil.")
					.define("enableNameTagCommand", true);
			nameTagCommandReplaceUnderscoresWithSpaces = builder
					.comment("If enabled, replaces underscores with spaces when using the /nametag command.")
					.define("nameTagCommandReplaceUnderscoresWithSpaces", true);
			
			builder.pop();
		}
	}
}