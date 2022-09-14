/*
 * This is the latest source code of Death Backup.
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

package com.natamus.deathbackup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> sendBackupReminderMessageToThoseWithAccessOnDeath;
		public final ForgeConfigSpec.ConfigValue<String> backupReminderMessage;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			sendBackupReminderMessageToThoseWithAccessOnDeath = builder
					.comment("When enabled, sends a message to a player when they die and have cheat-access that a backup has been created and can be loaded.")
					.define("sendBackupReminderMessageToThoseWithAccessOnDeath", true);
			backupReminderMessage = builder
					.comment("The message sent to players with cheat-access when 'sendBackupReminderMessageToThoseWithAccessOnDeath' is enabled.")
					.define("backupReminderMessage", "A backup of your inventory before your death has been created and can be loaded with '/deathbackup load 0'.");
			
			builder.pop();
		}
	}
}
