/*
 * This is the latest source code of Better Beacon Placement.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.betterbeaconplacement.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> breakBeaconBaseBlocks;
		public final ForgeConfigSpec.ConfigValue<Boolean> dropReplacedBlockTopBeacon;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			breakBeaconBaseBlocks = builder
					.comment("If enabled, drops all beacon base blocks when the beacon itself is broken.")
					.define("breakBeaconBaseBlocks", true);
			dropReplacedBlockTopBeacon = builder
					.comment("If enabled, when a mineral block replaces a normal block that block is dropped on top of the beacon.")
					.define("dropReplacedBlockTopBeacon", true);
			
			builder.pop();
		}
	}
}