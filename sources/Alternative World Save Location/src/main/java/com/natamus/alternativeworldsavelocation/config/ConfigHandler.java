/*
 * This is the latest source code of Alternative World Save Location.
 * Minecraft version: 1.19.2, mod version: 1.9.
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

package com.natamus.alternativeworldsavelocation.config;

import com.natamus.collective.functions.DataFunctions;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

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
					.define("defaultMinecraftWorldSaveLocation", DataFunctions.getGameDirectory() + File.separator + "saves");
			
			changeDefaultWorldBackupLocation = builder
					.comment("Disabled by default. Enable this to set a specific world backup folder. If disabled, this will be set to 'defaultMinecraftWorldSaveLocation'/_Backup.")
					.define("changeDefaultWorldBackupLocation", false);
			defaultMinecraftWorldBackupLocation = builder
					.comment("Use either \\\\ or / as a path separator. The world backup folder if both 'changeDefaultWorldSaveLocation' and 'changeDefaultWorldBackupLocation' are enabled.")
					.define("defaultMinecraftWorldBackupLocation", DataFunctions.getGameDirectory() + File.separator + "backups");
			
			builder.pop();
		}
	}
}