/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.3, mod version: 2.2.
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

package com.natamus.grindstonesharpertools.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> usesAfterGrinding;
		public final ForgeConfigSpec.ConfigValue<Double> sharpenedDamageModifier;
		public final ForgeConfigSpec.ConfigValue<Boolean> infiniteCreativeUses;
		
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
			infiniteCreativeUses = builder
					.comment("Whether to decrease sharpened uses in creative.")
					.define("infiniteCreativeUses", false);

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