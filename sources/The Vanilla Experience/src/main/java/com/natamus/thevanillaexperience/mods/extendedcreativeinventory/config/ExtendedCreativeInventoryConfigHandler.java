/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.extendedcreativeinventory.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExtendedCreativeInventoryConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> extendedItemGroupIndex;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			extendedItemGroupIndex = builder
					.comment("The index of the 'Extended' creative tab. Can be changed if another mod already uses the default fifth spot (index 4).")
					.defineInRange("extendedItemGroupIndex", 4, 0, 100);
			
			builder.pop();
		}
	}
}