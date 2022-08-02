/*
 * This is the latest source code of GUI Compass.
 * Minecraft version: 1.19.1, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.guicompass.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<String> guiCompassFormat;
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHaveCompassInInventory;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> compassPositionIsLeft;
		public final ForgeConfigSpec.ConfigValue<Boolean> compassPositionIsCenter;
		public final ForgeConfigSpec.ConfigValue<Boolean> compassPositionIsRight;
		
		public final ForgeConfigSpec.ConfigValue<Integer> compassHeightOffset;
		
		public final ForgeConfigSpec.ConfigValue<Integer> RGB_R;
		public final ForgeConfigSpec.ConfigValue<Integer> RGB_G;
		public final ForgeConfigSpec.ConfigValue<Integer> RGB_B;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			guiCompassFormat = builder
					.comment("What of the GUI compass should be displayed. Default: [FXYZ]. F: facing (direction), X: x coord, Y: y coord (depth), Z: z coord.")
					.define("guiCompassFormat", "FXYZ");
			mustHaveCompassInInventory = builder
					.comment("When enabled, will only show the GUI compass when a compass is present in the inventory.")
					.define("mustHaveCompassInInventory", true);
			
			compassPositionIsLeft = builder
					.comment("Places the GUI compass on the left.")
					.define("compassPositionIsLeft", true);
			compassPositionIsCenter = builder
					.comment("Places the GUI compass in the middle.")
					.define("compassPositionIsCenter", false);
			compassPositionIsRight = builder
					.comment("Places the GUI compass on the right.")
					.define("compassPositionIsRight", false);

			compassHeightOffset = builder
					.comment("The vertical offset (y coord) for the Compass. This determines how far down the time should be on the screen. Can be changed to prevent GUIs from overlapping.")
					.defineInRange("compassHeightOffset", 5, 0, 3000);
			
			RGB_R = builder
					.comment("The red RGB value for the compass text.")
					.defineInRange("RGB_R", 255, 0, 255);
			RGB_G = builder
					.comment("The green RGB value for the compass text.")
					.defineInRange("RGB_G", 255, 0, 255);
			RGB_B = builder
					.comment("The blue RGB value for the compass text.")
					.defineInRange("RGB_B", 255, 0, 255);
			
			builder.pop();
		}
	}
}