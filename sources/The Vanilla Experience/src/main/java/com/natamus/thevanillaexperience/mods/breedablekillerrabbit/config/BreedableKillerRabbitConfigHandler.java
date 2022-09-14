/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.breedablekillerrabbit.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BreedableKillerRabbitConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Double> chanceBabyRabbitIsKiller;
		public final ForgeConfigSpec.ConfigValue<Boolean> removeKillerRabbitNameTag;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			chanceBabyRabbitIsKiller = builder
					.comment("The chance that a baby rabbit is of the killer variant.")
					.defineInRange("chanceBabyRabbitIsKiller", 0.5, 0, 1.0);
			removeKillerRabbitNameTag = builder
					.comment("Remove the name 'The Killer Bunny' from the baby killer rabbit.")
					.define("removeKillerRabbitNameTag", true);
			
			builder.pop();
		}
	}
}