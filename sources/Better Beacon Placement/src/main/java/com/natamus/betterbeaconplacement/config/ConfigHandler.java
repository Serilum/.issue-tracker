/*
 * This is the latest source code of Better Beacon Placement.
 * Minecraft version: 1.19.0, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Better Beacon Placement ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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