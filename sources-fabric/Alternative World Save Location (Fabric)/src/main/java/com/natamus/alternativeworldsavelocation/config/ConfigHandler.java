/*
 * This is the latest source code of Alternative World Save Location.
 * Minecraft version: 1.19.2, mod version: 1.8.
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

import java.io.File;
import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_changeDefaultWorldSaveLocation;
	@Entry public static boolean changeDefaultWorldSaveLocation = false;

	@Comment public static Comment DESC_defaultMinecraftWorldSaveLocation;
	@Entry public static String defaultMinecraftWorldSaveLocation = System.getProperty("user.dir") + File.separator + "saves";

	@Comment public static Comment DESC_changeDefaultWorldBackupLocation;
	@Entry public static boolean changeDefaultWorldBackupLocation = false;

	@Comment public static Comment DESC_defaultMinecraftWorldBackupLocation;
	@Entry public static String defaultMinecraftWorldBackupLocation = System.getProperty("user.dir") + File.separator + "backups";
}