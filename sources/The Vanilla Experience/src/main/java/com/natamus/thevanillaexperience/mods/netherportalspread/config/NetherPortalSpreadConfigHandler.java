/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.netherportalspread.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NetherPortalSpreadConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnPortalCreation;
		public final ForgeConfigSpec.ConfigValue<String> messageOnPortalCreation;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnPreventSpreadBlocksFound;
		public final ForgeConfigSpec.ConfigValue<String> messageOnPreventSpreadBlocksFound;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnPortalBroken;
		public final ForgeConfigSpec.ConfigValue<String> messageOnPortalBroken;
		public final ForgeConfigSpec.ConfigValue<Boolean> prefixPortalCoordsInMessage;
		
		public final ForgeConfigSpec.ConfigValue<Integer> portalSpreadRadius;
		public final ForgeConfigSpec.ConfigValue<Integer> spreadDelayTicks;
		public final ForgeConfigSpec.ConfigValue<Integer> instantConvertAmount;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> preventSpreadWithBlock;
		public final ForgeConfigSpec.ConfigValue<Integer> preventSpreadBlockAmountNeeded;
		public final ForgeConfigSpec.ConfigValue<String> preventSpreadBlockString;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			sendMessageOnPortalCreation = builder
					.comment("When enabled, sends a message to players around the portal that the nether is spreading and that you can stop the spread with 'preventSpreadBlockAmountNeeded' of the 'preventSpreadBlockString' block.")
					.define("sendMessageOnPortalCreation", true);
			messageOnPortalCreation = builder
					.comment("The message sent on portal creation.")
					.define("messageOnPortalCreation", "You feel a corrupted energy coming from the portal. The nether will slowly spread into the overworld unless %preventSpreadBlockAmountNeeded% %preventSpreadBlockString% are placed within a %portalSpreadRadius% block radius around the portal.");
			sendMessageOnPreventSpreadBlocksFound = builder
					.comment("When enabled, sends a message to players around the portal that the nether spread has stopped when the portal detects new 'preventSpreadBlockString' blocks.")
					.define("sendMessageOnPreventSpreadBlocksFound", true);
			messageOnPreventSpreadBlocksFound = builder
					.comment("The message sent on preventspread blocks found.")
					.define("messageOnPreventSpreadBlocksFound", "With enough %preventSpreadBlockString% placed, you feel the corrupted energy fade.");
			sendMessageOnPortalBroken = builder
					.comment("When enabled, sends a message to players around the portal when it is broken.")
					.define("sendMessageOnPortalBroken", true);
			messageOnPortalBroken = builder
					.comment("The message sent when a portal is broken.")
					.define("messageOnPortalBroken", "With the nether portal broken, the corrupted energy is no longer able to enter the overworld.");
			prefixPortalCoordsInMessage = builder
					.comment("When enabled, shows the portal coordinates in portal messages.")
					.define("prefixPortalCoordsInMessage", true);
			
			portalSpreadRadius = builder
					.comment("The radius around the portal to which the nether blocks can spread.")
					.defineInRange("portalSpreadRadius", 30, 1, 100);
			spreadDelayTicks = builder
					.comment("The delay in ticks in between the spread around the nether portal. 20 ticks = 1 second.")
					.defineInRange("spreadDelayTicks", 40, 1, 72000);
			instantConvertAmount = builder
					.comment("The amount of blocks that are instantly converted to a nether block around a portal when it is detected. If there are existing nether blocks within the radius, their count is substracted from this number.")
					.defineInRange("instantConvertAmount", 50, 0, 1000);
			
			preventSpreadWithBlock = builder
					.comment("When enabled, blocks the spread effect when there are n (defined) prevent-spread-blocks (defined) within the radius.")
					.define("preventSpreadWithBlock", true);
			preventSpreadBlockAmountNeeded = builder
					.comment("The amount of prevent-spread-blocks (defined) needed within the radius of the nether portal to prevent spread.")
					.define("preventSpreadBlockAmountNeeded", 4);
			preventSpreadBlockString = builder
					.comment("The block which prevents the nether portal from spreading. By default a coal block (minecraft:coal_block is the namespace ID).")
					.define("preventSpreadBlockString", "minecraft:coal_block");

			
			builder.pop();
		}
	}
}