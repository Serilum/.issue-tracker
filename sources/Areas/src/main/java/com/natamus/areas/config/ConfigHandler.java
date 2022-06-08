/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.0, mod version: 2.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Areas ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.areas.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final Hud HUD = new Hud(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> giveUnnamedAreasRandomName;
		public final ForgeConfigSpec.ConfigValue<Integer> radiusAroundPlayerToCheckForSigns;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> sendChatMessages;
		public final ForgeConfigSpec.ConfigValue<Boolean> showHUDMessages;
		
		public final ForgeConfigSpec.ConfigValue<String> joinPrefix;
		public final ForgeConfigSpec.ConfigValue<String> joinSuffix;
		public final ForgeConfigSpec.ConfigValue<String> leavePrefix;
		public final ForgeConfigSpec.ConfigValue<String> leaveSuffix;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			giveUnnamedAreasRandomName = builder
					.comment("When enabled, gives signs without an area name a randomly chosen one from a preset list.")
					.define("giveUnnamedAreasRandomName", true);
			radiusAroundPlayerToCheckForSigns = builder
					.comment("The radius in blocks around the player in which to check for area signs.")
					.defineInRange("radiusAroundPlayerToCheckForSigns", 100, 0, 1000);
			
			sendChatMessages = builder
					.comment("When enabled, sends the player the area notifications in chat.")
					.define("sendChatMessages", false);
			showHUDMessages = builder
					.comment("When enabled, sends the player the area notifications in the HUD on screen.")
					.define("showHUDMessages", true);
			
			joinPrefix = builder
					.comment("The prefix of the message whenever a player enters an area.")
					.define("joinPrefix", "Entering ");
			joinSuffix = builder
					.comment("The suffix of the message whenever a player enters an area.")
					.define("joinSuffix", ".");
			leavePrefix = builder
					.comment("The prefix of the message whenever a player leaves an area.")
					.define("leavePrefix", "Leaving ");
			leaveSuffix = builder
					.comment("The suffix of the message whenever a player leaves an area.")
					.define("leaveSuffix", ".");
			
			builder.pop();
		}
	}
	
	public static class Hud {
		public final ForgeConfigSpec.ConfigValue<Boolean> HUDOnlyAreaName;
		public final ForgeConfigSpec.ConfigValue<Integer> HUDMessageFadeDelayMs;
		public final ForgeConfigSpec.ConfigValue<Integer> HUDMessageHeightOffset;
		
		public final ForgeConfigSpec.ConfigValue<Double> HUD_FontSizeScaleModifier;
		public final ForgeConfigSpec.ConfigValue<Integer> HUD_RGB_R;
		public final ForgeConfigSpec.ConfigValue<Integer> HUD_RGB_G;
		public final ForgeConfigSpec.ConfigValue<Integer> HUD_RGB_B;
		
		public Hud(ForgeConfigSpec.Builder builder) {
			builder.push("Hud");

			HUDOnlyAreaName = builder
					.comment("When enabled, only shows the areaname in the HUD. When disabled, the prefixes and suffices will also be used.")
					.define("HUDOnlyAreaName", false);
			
			HUDMessageFadeDelayMs = builder
					.comment("The delay in ms after which the HUD message should fade out.")
					.defineInRange("HUDMessageFadeDelayMs", 4000, 100, 360000);
			HUDMessageHeightOffset = builder
					.comment("The vertical offset (y coord) for the HUD message. This determines how far down the message should be on the screen. Can be changed to prevent GUIs from overlapping.")
					.defineInRange("HUDMessageHeightOffset", 10, 0, 3000);
			
			HUD_FontSizeScaleModifier = builder
					.comment("Increases the font size of the text in the HUD message. If you change this value, make sure to test the different GUI scale settings in-game. 6.0 is considered large.")
					.defineInRange("HUD_FontModifier", 1.0, 0, 10.0);
			HUD_RGB_R = builder
					.comment("The red RGB value for the HUD message.")
					.defineInRange("HUD_RGB_R", 100, 0, 255);
			HUD_RGB_G = builder
					.comment("The green RGB value for the HUD message.")
					.defineInRange("HUD_RGB_G", 200, 0, 255);
			HUD_RGB_B = builder
					.comment("The blue RGB value for the HUD message.")
					.defineInRange("HUD_RGB_B", 50, 0, 255);
			
			builder.pop();
		}
	}
}