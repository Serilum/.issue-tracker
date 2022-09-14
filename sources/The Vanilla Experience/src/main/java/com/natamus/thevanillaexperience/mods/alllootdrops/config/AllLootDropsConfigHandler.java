/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.alllootdrops.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AllLootDropsConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> lootQuantity;
		public final ForgeConfigSpec.ConfigValue<Boolean> lootingEnchantAffectsQuantity;
		public final ForgeConfigSpec.ConfigValue<Double> lootingEnchantExtraQuantityChance;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			lootQuantity = builder
					.comment("Determines the amount of loot dropped by each mob.")
					.defineInRange("lootQuantity", 1, 1, 128);
			lootingEnchantAffectsQuantity = builder
					.comment("If enabled, the looting enchant has a chance to increase the quantity of loot dropped. Per level a roll is done with the chance from 'lootingEnchantExtraQuantityChancePerLevel'. If the roll succeeds, 1 is added to the loot amount.")
					.define("lootingEnchantAffectsQuantity", true);
			lootingEnchantExtraQuantityChance = builder
					.comment("The chance a roll succeeds in adding 1 to the total loot amount.")
					.defineInRange("lootingEnchantExtraQuantityChance", 0.5, 0, 1.0);
			
			builder.pop();
		}
	}
}