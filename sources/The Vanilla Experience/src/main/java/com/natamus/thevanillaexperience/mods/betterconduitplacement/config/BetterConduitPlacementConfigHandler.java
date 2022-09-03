/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.betterconduitplacement.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BetterConduitPlacementConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> breakConduitBlocks;
		public final ForgeConfigSpec.ConfigValue<Boolean> dropReplacedBlockTopConduit;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			breakConduitBlocks = builder
					.comment("If enabled, drops all conduit blocks when the conduit itself is broken.")
					.define("breakConduitBlocks", true);
			dropReplacedBlockTopConduit = builder
					.comment("If enabled, when prismarine replaces a normal block that block is dropped on top of the conduit.")
					.define("dropReplacedBlockTopConduit", true);
			
			builder.pop();
		}
	}
}