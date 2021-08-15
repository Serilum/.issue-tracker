/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.guiclock.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GUIClockConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {	
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHaveClockInInventoryForGameTime;
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHaveClockInInventoryForRealTime;
		public final ForgeConfigSpec.ConfigValue<Boolean> _24hourformat;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> showOnlyMinecraftClockIcon;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> showBothTimes;
		public final ForgeConfigSpec.ConfigValue<Boolean> showRealTime;
		public final ForgeConfigSpec.ConfigValue<Boolean> showRealTimeSeconds;
		public final ForgeConfigSpec.ConfigValue<Boolean> showDaysPlayedWorld;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> clockPositionIsLeft;
		public final ForgeConfigSpec.ConfigValue<Boolean> clockPositionIsCenter;
		public final ForgeConfigSpec.ConfigValue<Boolean> clockPositionIsRight;
		
		public final ForgeConfigSpec.ConfigValue<Integer> clockHeightOffset;
		public final ForgeConfigSpec.ConfigValue<Integer> clockWidthOffset;
		
		public final ForgeConfigSpec.ConfigValue<Integer> RGB_R;
		public final ForgeConfigSpec.ConfigValue<Integer> RGB_G;
		public final ForgeConfigSpec.ConfigValue<Integer> RGB_B;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHaveClockInInventoryForGameTime = builder
					.comment("When enabled, will only show the game time when a clock is present in the inventory.")
					.define("mustHaveClockInInventoryForGameTime", true);
			mustHaveClockInInventoryForRealTime = builder
					.comment("When enabled, will only show the real time when a clock is present in the inventory.")
					.define("mustHaveClockInInventoryForRealTime", true);
			_24hourformat = builder
					.comment("Sets the format of the clock to the 24-hour format.")
					.define("_24hourformat", true);
			
			showOnlyMinecraftClockIcon = builder
					.comment("When enabled, shows the clock item icon instead of a clock with numbers.")
					.define("showOnlyMinecraftClockIcon", false);
			
			showBothTimes = builder
					.comment("Show both in-game time and real local time.")
					.define("showBothTimes", false);
			showRealTime = builder
					.comment("Show actual local time instead of in-game time.")
					.define("showRealTime", false);
			showRealTimeSeconds = builder
					.comment("Show the seconds in the clock.")
					.define("showRealTimeSeconds", false);
			showDaysPlayedWorld = builder
					.comment("Show the days played in the world.")
					.define("showDaysPlayedWorld", true);
			
			clockPositionIsLeft = builder
					.comment("Places the GUI clock on the left.")
					.define("clockPositionIsLeft", false);
			clockPositionIsCenter = builder
					.comment("Places the GUI clock in the middle.")
					.define("clockPositionIsCenter", false);
			clockPositionIsRight = builder
					.comment("Places the GUI clock on the right.")
					.define("clockPositionIsRight", true);

			clockHeightOffset = builder
					.comment("The vertical offset (y coord) for the Clock. This determines how far down the time should be on the screen. Can be changed to prevent GUIs from overlapping.")
					.defineInRange("clockHeightOffset", 5, 0, 3000);
			clockWidthOffset = builder
					.comment("The horizontal offset (x coord) for the Clock.")
					.defineInRange("clockWidthOffset", 0, -3000, 3000);
			
			RGB_R = builder
					.comment("The red RGB value for the clock text.")
					.defineInRange("RGB_R", 255, 0, 255);
			RGB_G = builder
					.comment("The green RGB value for the clock text.")
					.defineInRange("RGB_G", 255, 0, 255);
			RGB_B = builder
					.comment("The blue RGB value for the clock text.")
					.defineInRange("RGB_B", 255, 0, 255);
			
			builder.pop();
		}
	}
}