/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.1, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.grindstonesharpertools.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> usesAfterGrinding;
		public final ForgeConfigSpec.ConfigValue<Double> sharpenedDamageModifier;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> sendUsesLeftInChat;
		public final ForgeConfigSpec.ConfigValue<Boolean> showUsesLeftInItemName;
		
		public final ForgeConfigSpec.ConfigValue<String> nameUsesPrefix;
		public final ForgeConfigSpec.ConfigValue<String> nameUsesSuffix;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			usesAfterGrinding = builder
					.comment("The amount of sharper uses a tool has after using it on the grindstone.")
					.defineInRange("usesAfterGrinding", 32, 1, 5000);
			sharpenedDamageModifier = builder
					.comment("The damage modifier of sharpened tools.")
					.defineInRange("sharpenedDamageModifier", 1.5, 0, 100.0);

			sendUsesLeftInChat = builder
					.comment("Sends the sharpened tool user a message at 75%, 50%, 25%, 10%.")
					.define("sendUsesLeftInChat", true);
			showUsesLeftInItemName = builder
					.comment("Shows the uses left in the name of the item.")
					.define("showUsesLeftInItemName", true);
			
			nameUsesPrefix = builder
					.comment("The prefix of the sharpened uses left in the tool name.")
					.define("nameUsesPrefix", "(Sharpened uses: ");
			nameUsesSuffix = builder
					.comment("The suffix of the sharpened uses left in the tool name.")
					.define("nameUsesSuffix", ")");
			
			builder.pop();
		}
	}
}