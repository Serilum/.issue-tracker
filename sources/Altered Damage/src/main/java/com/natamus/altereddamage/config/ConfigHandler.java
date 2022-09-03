/*
 * This is the latest source code of Altered Damage.
 * Minecraft version: 1.19.2, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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