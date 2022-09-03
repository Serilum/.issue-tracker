/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.deathbackup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DeathBackupConfigHandler {
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
