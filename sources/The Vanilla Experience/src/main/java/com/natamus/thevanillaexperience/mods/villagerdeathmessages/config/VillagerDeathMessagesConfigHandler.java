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

package com.natamus.thevanillaexperience.mods.villagerdeathmessages.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class VillagerDeathMessagesConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> showLocation;
		public final ForgeConfigSpec.ConfigValue<Boolean> mentionModdedVillagers;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			showLocation = builder
					.comment("If enabled, shows the location of the villager in the death message.")
					.define("showLocation", true);
			mentionModdedVillagers = builder
					.comment("If enabled, also shows death messages of modded villagers. If you've found a 'villager'-entity that isn't named let me know by opening an issue so I can add it in.")
					.define("mentionModdedVillagers", true);
			
			builder.pop();
		}
	}
}