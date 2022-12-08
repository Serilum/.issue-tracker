/*
 * This is the latest source code of Name Tag Tweaks.
 * Minecraft version: 1.19.3, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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