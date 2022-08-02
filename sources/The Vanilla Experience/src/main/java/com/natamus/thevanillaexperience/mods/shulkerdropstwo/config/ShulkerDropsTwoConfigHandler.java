/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.shulkerdropstwo.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShulkerDropsTwoConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> alwaysDropShells;
		public final ForgeConfigSpec.ConfigValue<Integer> shulkerDropAmount;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			alwaysDropShells = builder
					.comment("Ignore the drop chance (default 50%) that a Shulker will drop their shell and instead makes them always drop it.")
					.define("alwaysDropShells", false);
			shulkerDropAmount = builder
					.comment("Sets the amount of shells Shulkers drop.")
					.defineInRange("shulkerDropAmount", 2, 1, 64);
			
			builder.pop();
		}
	}
}