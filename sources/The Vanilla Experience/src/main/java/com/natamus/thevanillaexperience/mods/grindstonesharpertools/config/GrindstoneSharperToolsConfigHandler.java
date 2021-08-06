/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.grindstonesharpertools.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GrindstoneSharperToolsConfigHandler {
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