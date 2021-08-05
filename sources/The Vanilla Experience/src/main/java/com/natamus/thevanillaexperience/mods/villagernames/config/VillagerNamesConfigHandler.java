/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
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