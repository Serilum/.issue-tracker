/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.villagernames.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class VillagerNamesConfigHandler {
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