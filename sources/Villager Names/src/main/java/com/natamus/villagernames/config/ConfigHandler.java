/*
 * This is the latest source code of Villager Names.
 * Minecraft version: 1.19.2, mod version: 3.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.villagernames.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> _useCustomNames;
		public final ForgeConfigSpec.ConfigValue<Boolean> _useFemaleNames;
		public final ForgeConfigSpec.ConfigValue<Boolean> _useMaleNames;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> nameModdedVillagers;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			_useCustomNames = builder
					.comment("Use the custom name list, editable in ./mods/villagernames/customnames.txt, seperated by a comma.")
					.define("_useCustomNames", true);
			_useFemaleNames = builder
					.comment("Use the list of pre-defined female names when naming villagers.")
					.define("_useFemaleNames", true);
			_useMaleNames = builder
					.comment("Use the list of pre-defined male names when naming villagers.")
					.define("_useMaleNames", true);
			
			nameModdedVillagers = builder
					.comment("If enabled, also gives modded villagers a name. If you've found a 'villager'-entity that isn't named let me know by opening an issue so I can add it in.")
					.define("nameModdedVillagers", true);
			
			builder.pop();
		}
	}
}