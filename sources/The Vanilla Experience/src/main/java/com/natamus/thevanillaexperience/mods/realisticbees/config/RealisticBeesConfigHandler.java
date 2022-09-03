/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.realisticbees.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RealisticBeesConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Boolean> beesDieFromStingingPlayer;
		public final ForgeConfigSpec.ConfigValue<Boolean> beesDieFromStingingMob;
		public final ForgeConfigSpec.ConfigValue<Boolean> beesLeaveTheirStinger;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendStungPlayerWithStingerAMessage;
		
		public final ForgeConfigSpec.ConfigValue<Double> chanceBeeLeavesItsStinger;
		public final ForgeConfigSpec.ConfigValue<Double> chanceBeeStingerIsPulledOut;
		
		public final ForgeConfigSpec.ConfigValue<Integer> timeInSecondsStingerPumpsPoison;
		public final ForgeConfigSpec.ConfigValue<Integer> timeInSecondsBeeWithoutStingerDies;
		
		public final ForgeConfigSpec.ConfigValue<Integer> extraBeeSpawnsPerBee;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			beesDieFromStingingPlayer = builder
					.comment("When enabled, bees die after stinging a player. This takes roughly a few minutes.")
					.define("beesDieFromStingingPlayer", true);
			beesDieFromStingingMob = builder
					.comment("When enabled, bees die after stinging a mob. This takes roughly a few minutes.")
					.define("beesDieFromStingingMob", true);
			beesLeaveTheirStinger = builder
					.comment("After a bee stings, it has a chance to leave its stinger inside the entity. It must be pulled out.")
					.define("beesLeaveTheirStinger", true);
			sendStungPlayerWithStingerAMessage = builder
					.comment("After a bee stings a player and its stinger is left behind, send the player a message.")
					.define("sendStungPlayerWithStingerAMessage", true);
			
			chanceBeeLeavesItsStinger = builder
					.comment("The chance the bee's stinger lodges in the stung entity, resulting in death.")
					.defineInRange("chanceBeeLeavesItsStinger", 0.33, 0, 1.0);			
			chanceBeeStingerIsPulledOut = builder
					.comment("The chance the bee's stinger is pulled out by a player after right-clicking shears.")
					.defineInRange("chanceBeeStingerIsPulledOut", 0.5, 0, 1.0);
			
			timeInSecondsStingerPumpsPoison = builder
					.comment("The time in seconds a stinger that's left in an entity continues to pump poison.")
					.defineInRange("timeInSecondsStingerPumpsPoison", 30, 0, 300);
			timeInSecondsBeeWithoutStingerDies = builder
					.comment("The time in a seconds after a bee without its stinger dies.")
					.defineInRange("timeInSecondsBeeWithoutStingerDies", 60, 0, 600);
			
			extraBeeSpawnsPerBee = builder
					.comment("In order to make bees a little more common. Whenever a bee naturally spawns, the mod spawns an additional 'extraBeeSpawnsPerBee' bees.")
					.defineInRange("extraBeeSpawnsPerBee", 4, 0, 50);
			
			builder.pop();
		}
	}
}