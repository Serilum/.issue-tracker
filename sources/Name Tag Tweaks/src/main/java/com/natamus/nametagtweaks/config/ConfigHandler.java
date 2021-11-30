/*
 * This is the latest source code of Name Tag Tweaks.
 * Minecraft version: 1.18.0, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Name Tag Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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