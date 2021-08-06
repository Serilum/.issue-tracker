/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
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

package com.natamus.thevanillaexperience.mods.guifollowers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GUIFollowersConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<String> followerListHeaderFormat;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> showFollowerHealth;
		public final ForgeConfigSpec.ConfigValue<String> followerHealthFormat;
		public final ForgeConfigSpec.ConfigValue<Boolean> showFollowerDistance;
		public final ForgeConfigSpec.ConfigValue<String> followerDistanceFormat;
		
		public final ForgeConfigSpec.ConfigValue<Integer> distanceToCheckForFollowersAround;
		public final ForgeConfigSpec.ConfigValue<Integer> timeBetweenChecksInSeconds;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> followerListPositionIsLeft;
		public final ForgeConfigSpec.ConfigValue<Boolean> followerListPositionIsCenter;
		public final ForgeConfigSpec.ConfigValue<Boolean> followerListPositionIsRight;
		
		public final ForgeConfigSpec.ConfigValue<Integer> followerListHeightOffset;
		
		public final ForgeConfigSpec.ConfigValue<Integer> RGB_R;
		public final ForgeConfigSpec.ConfigValue<Integer> RGB_G;
		public final ForgeConfigSpec.ConfigValue<Integer> RGB_B;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			followerListHeaderFormat = builder
					.comment("The header text above the follower list.")
					.define("followerListHeaderFormat", "Followers:");			
			
			showFollowerHealth = builder
					.comment("If enabled, shows the follower's health in the GUI list.")
					.define("showFollowerHealth", true);
			followerHealthFormat = builder
					.comment("The format of the health string in the GUI. <health> will be replaced by the percentage of total health.")
					.define("followerHealthFormat", ": <health>%");
			showFollowerDistance = builder
					.comment("If enabled, shows the follower's distance in blocks to the player in the GUI list.")
					.define("showFollowerDistance", true);
			followerDistanceFormat = builder
					.comment("The format of the distance string in the GUI. <distance> will be replaced by distance in blocks.")
					.define("followerDistanceFormat", " (<distance> blocks)");
			
			distanceToCheckForFollowersAround = builder
					.comment("The distance in blocks around the player where the mod checks for tamed, non-sitting followers to add to the list. A value of -1 disables this feature.")
					.defineInRange("distanceToCheckForFollowersAround", 50, -1, 300);
			timeBetweenChecksInSeconds = builder
					.comment("The time in seconds in between checking for tamed, non-sitting followers around the player.")
					.defineInRange("timeBetweenChecksInSeconds", 2, 0, 3600);
			
			followerListPositionIsLeft = builder
					.comment("Places the follower list on the left.")
					.define("followerListPositionIsLeft", true);
			followerListPositionIsCenter = builder
					.comment("Places the follower list in the middle.")
					.define("followerListPositionIsCenter", false);
			followerListPositionIsRight = builder
					.comment("Places the follower list on the right.")
					.define("followerListPositionIsRight", false);

			followerListHeightOffset = builder
					.comment("The vertical offset (y coord) for the follower list. This determines how far down the list should be on the screen. Can be changed to prevent GUIs from overlapping.")
					.defineInRange("followerListHeightOffset", 20, 0, 3000);
			
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