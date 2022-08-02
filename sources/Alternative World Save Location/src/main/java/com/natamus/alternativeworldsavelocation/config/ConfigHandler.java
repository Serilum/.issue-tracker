/*
 * This is the latest source code of Alternative World Save Location.
 * Minecraft version: 1.19.1, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.alternativeworldsavelocation.config;

import java.io.File;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> changeDefaultWorldSaveLocation;
		public final ForgeConfigSpec.ConfigValue<String> defaultMinecraftWorldSaveLocation;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> changeDefaultWorldBackupLocation;
		public final ForgeConfigSpec.ConfigValue<String> defaultMinecraftWorldBackupLocation;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			changeDefaultWorldSaveLocation = builder
					.comment("Disabled by default, to prevent unwanted modpack behaviour. When enabled, changes the world location to 'defaultMinecraftWorldSaveLocation'")
					.define("changeDefaultWorldSaveLocation", false);
			defaultMinecraftWorldSaveLocation = builder
					.comment("Use either \\\\ or / as a path separator. The location of the folder containing the world saves.")
					.define("defaultMinecraftWorldSaveLocation", System.getProperty("user.dir") + File.separator + "saves");
			
			changeDefaultWorldBackupLocation = builder
					.comment("Disabled by default. Enable this to set a specific world backup folder. If disabled, this will be set to 'defaultMinecraftWorldSaveLocation'/_Backup.")
					.define("changeDefaultWorldBackupLocation", false);
			defaultMinecraftWorldBackupLocation = builder
					.comment("Use either \\\\ or / as a path separator. The world backup folder if both 'changeDefaultWorldSaveLocation' and 'changeDefaultWorldBackupLocation' are enabled.")
					.define("defaultMinecraftWorldBackupLocation", System.getProperty("user.dir") + File.separator + "backups");
			
			builder.pop();
		}
	}
}