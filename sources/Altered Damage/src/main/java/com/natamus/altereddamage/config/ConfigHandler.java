/*
 * This is the latest source code of Altered Damage.
 * Minecraft version: 1.19.1, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Altered Damage ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.altereddamage.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> preventFatalModifiedDamage;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> alterEntityDamageTaken;
		public final ForgeConfigSpec.ConfigValue<Boolean> alterPlayerDamageTaken;
		public final ForgeConfigSpec.ConfigValue<Double> entityDamageModifier;
		public final ForgeConfigSpec.ConfigValue<Double> playerDamageModifier;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			preventFatalModifiedDamage = builder
					.comment("When enabled, does not change the damage output if it would be fatal with the modifier and not fatal without. Prevents dying from for example poison and starvation.")
					.define("preventFatalModifiedDamage", true);	
			
			alterEntityDamageTaken = builder
					.comment("If enabled, modifies the damage another entity receives by the global modifier.")
					.define("alterEntityDamageTaken", true);
			alterPlayerDamageTaken = builder
					.comment("If enabled, modifies the damage a player receives by the global modifier.")
					.define("alterPlayerDamageTaken", true);
			entityDamageModifier = builder
					.comment("The global damage modifier for other entities.")
					.defineInRange("entityDamageModifier", 2.0, 0.01, 20.0);
			playerDamageModifier = builder
					.comment("The global damage modifier for players.")
					.defineInRange("playerDamageModifier", 2.0, 0.01, 20.0 );
			
			builder.pop();
		}
	}
}