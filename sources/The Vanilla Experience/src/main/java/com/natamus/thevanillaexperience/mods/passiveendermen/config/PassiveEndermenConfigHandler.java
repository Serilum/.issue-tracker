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

package com.natamus.thevanillaexperience.mods.passiveendermen.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PassiveEndermenConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> preventEndermenFromTeleporting;
		public final ForgeConfigSpec.ConfigValue<Boolean> preventEndermenFromGriefing;
		public final ForgeConfigSpec.ConfigValue<Boolean> preventEndermenFromAttackingFirst;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			preventEndermenFromTeleporting = builder
					.comment("If enabled, prevents the endermen from teleporting.")
					.define("preventEndermenFromTeleporting", true);
			preventEndermenFromGriefing = builder
					.comment("If enabled, prevents from picking up and placing blocks.")
					.define("preventEndermenFromGriefing", true);
			preventEndermenFromAttackingFirst = builder
					.comment("If enabled, stops the endermen from attacking first when a player looks at them. Also works for endermite.")
					.define("preventEndermenFromAttackingFirst", true);
			
			builder.pop();
		}
	}
}