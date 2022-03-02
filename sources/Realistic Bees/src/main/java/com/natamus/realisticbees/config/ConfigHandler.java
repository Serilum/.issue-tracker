/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.18.2, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Realistic Bees ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.realisticbees.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> beeSizeModifier;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> beesDieFromStingingPlayer;
		public final ForgeConfigSpec.ConfigValue<Boolean> beesDieFromStingingMob;
		public final ForgeConfigSpec.ConfigValue<Boolean> beesLeaveTheirStinger;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendStungPlayerWithStingerAMessage;
		
		public final ForgeConfigSpec.ConfigValue<Double> chanceBeeLeavesItsStinger;
		public final ForgeConfigSpec.ConfigValue<Double> chanceBeeStingerIsPulledOut;
		
		public final ForgeConfigSpec.ConfigValue<Integer> timeInSecondsStingerPumpsPoison;
		public final ForgeConfigSpec.ConfigValue<Integer> timeInSecondsBeeWithoutStingerDies;
		
		public final ForgeConfigSpec.ConfigValue<Integer> extraBeeSpawnsPerBee;
		public final ForgeConfigSpec.ConfigValue<Boolean> preventBeeSuffocationDamage;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			beeSizeModifier = builder
					.comment("This value determines the size of a bee. By default 0.25, which means they are 1/4th of their vanilla size. Set to 1.0 to disable the different bee size feature.")
					.defineInRange("beeSizeModifier", 0.25, 0.01, 5.0);		
			
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
					.defineInRange("extraBeeSpawnsPerBee", 9, 0, 50);
			preventBeeSuffocationDamage = builder
					.comment("The smaller (baby) bees can sometimes do something unexpected with their AI and get suffocation damage. Preventing this damage fixes them disappearing.")
					.define("preventBeeSuffocationDamage", true);
			
			builder.pop();
		}
	}
}