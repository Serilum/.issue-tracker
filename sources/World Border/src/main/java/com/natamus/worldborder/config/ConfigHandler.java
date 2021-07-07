/*
 * This is the latest source code of World Border.
 * Minecraft version: 1.16.5, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of World Border ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.worldborder.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final Borders BORDERS = new Borders(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> enableCustomOverworldBorder;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableCustomNetherBorder;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableCustomEndBorder;
	
		public final ForgeConfigSpec.ConfigValue<Boolean> shouldLoopToOppositeBorder;
		public final ForgeConfigSpec.ConfigValue<Integer> distanceTeleportedBack;
		
		public final ForgeConfigSpec.ConfigValue<String> nearBorderMessage;
		public final ForgeConfigSpec.ConfigValue<String> hitBorderMessage;
		public final ForgeConfigSpec.ConfigValue<String> loopBorderMessage;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			enableCustomOverworldBorder = builder
					.comment("When enabled, uses the overworldBorderCoords to set the border.")
					.define("enableCustomOverworldBorder", true);
			enableCustomNetherBorder = builder
					.comment("When enabled, uses the netherBorderCoords to set the border.")
					.define("enableCustomNetherBorder", false);
			enableCustomEndBorder = builder
					.comment("When enabled, uses the endBorderCoords to set the border.")
					.define("enableCustomEndBorder", true);
			
			shouldLoopToOppositeBorder = builder
					.comment("When enabled, instead of teleporting the player inside near where they were, teleports them from the positive to the negative x/z coord and vice versa.")
					.define("shouldLoopToOppositeBorder", true);
			distanceTeleportedBack = builder
					.comment("The amount of blocks the player is teleported inside after hitting the border.")
					.defineInRange("distanceTeleportedBack", 10, 0, 1000);
			
			nearBorderMessage = builder
					.comment("The message which will be sent to the player when they are within 'distanceTeleportedBack' to the world border.")
					.define("nearBorderMessage", "You're getting close to the world border!");
			hitBorderMessage = builder
					.comment("The message which will be sent to the player when they hit the world border.")
					.define("hitBorderMessage", "You've hit the world border, and were teleported inside!");
			loopBorderMessage = builder
					.comment("The message sent to the player when they hit the border and 'shouldLoopToOppositeBorder' is enabled.")
					.define("loopBorderMessage", "You've hit the world border, and have looped around the world!");
			
			builder.pop();
		}
	}

	public static class Borders {
		public final ForgeConfigSpec.ConfigValue<Integer> overworldBorderPositiveX;
		public final ForgeConfigSpec.ConfigValue<Integer> overworldBorderNegativeX;
		public final ForgeConfigSpec.ConfigValue<Integer> overworldBorderPositiveZ;
		public final ForgeConfigSpec.ConfigValue<Integer> overworldBorderNegativeZ;
		
		public final ForgeConfigSpec.ConfigValue<Integer> netherBorderPositiveX;
		public final ForgeConfigSpec.ConfigValue<Integer> netherBorderNegativeX;
		public final ForgeConfigSpec.ConfigValue<Integer> netherBorderPositiveZ;
		public final ForgeConfigSpec.ConfigValue<Integer> netherBorderNegativeZ;
		
		public final ForgeConfigSpec.ConfigValue<Integer> endBorderPositiveX;
		public final ForgeConfigSpec.ConfigValue<Integer> endBorderNegativeX;
		public final ForgeConfigSpec.ConfigValue<Integer> endBorderPositiveZ;
		public final ForgeConfigSpec.ConfigValue<Integer> endBorderNegativeZ;
		
		public Borders(ForgeConfigSpec.Builder builder) {
			builder.push("Borders");
			overworldBorderPositiveX = builder
					.comment("The overworld border located at the positive x coordinate.")
					.defineInRange("overworldBorderPositiveX", 5000, 0, 100000);
			overworldBorderNegativeX = builder
					.comment("The overworld border located at the negative x coordinate.")
					.defineInRange("overworldBorderNegativeX", -5000, -100000, 0);
			overworldBorderPositiveZ = builder
					.comment("The overworld border located at the positive z coordinate.")
					.defineInRange("overworldBorderPositiveZ", 5000, 0, 100000);
			overworldBorderNegativeZ = builder
					.comment("The overworld border located at the negative z coordinate.")
					.defineInRange("overworldBorderNegativeZ", -5000, -100000, 0);
			
			netherBorderPositiveX = builder
					.comment("The nether border located at the positive x coordinate.")
					.defineInRange("netherBorderPositiveX", 625, 0, 100000);
			netherBorderNegativeX = builder
					.comment("The nether border located at the negative x coordinate.")
					.defineInRange("netherBorderNegativeX", -625, -100000, 0);
			netherBorderPositiveZ = builder
					.comment("The nether border located at the positive z coordinate.")
					.defineInRange("netherBorderPositiveZ", 625, 0, 100000);
			netherBorderNegativeZ = builder
					.comment("The nether border located at the negative z coordinate.")
					.defineInRange("netherBorderNegativeZ", -625, -100000, 0);
			
			endBorderPositiveX = builder
					.comment("The end border located at the positive x coordinate.")
					.defineInRange("endBorderPositiveX", 5000, 0, 100000);
			endBorderNegativeX = builder
					.comment("The end border located at the negative x coordinate.")
					.defineInRange("endBorderNegativeX", -5000, -100000, 0);
			endBorderPositiveZ = builder
					.comment("The end border located at the positive z coordinate.")
					.defineInRange("endBorderPositiveZ", 5000, 0, 100000);
			endBorderNegativeZ = builder
					.comment("The end border located at the negative z coordinate.")
					.defineInRange("endBorderNegativeZ", -5000, -100000, 0);
			
			builder.pop();
		}
	}
}