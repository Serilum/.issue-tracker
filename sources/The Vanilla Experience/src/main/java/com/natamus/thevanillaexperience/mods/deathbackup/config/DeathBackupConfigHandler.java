/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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
