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

package com.natamus.thevanillaexperience.mods.despawningeggshatch.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DespawningEggsHatchConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> eggOnlyHatchesWhenOnTopOfHayBlock;
		public final ForgeConfigSpec.ConfigValue<Double> eggWillHatchChance;
		public final ForgeConfigSpec.ConfigValue<Integer> onlyHatchIfLessChickensAroundThan;
		public final ForgeConfigSpec.ConfigValue<Integer> radiusEntityLimiterCheck;
		public final ForgeConfigSpec.ConfigValue<Boolean> newHatchlingIsBaby;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			eggOnlyHatchesWhenOnTopOfHayBlock = builder
					.comment("When enabled, an egg will only hatch when it is laid on top a hay block. This prevents wild chicken colonies from expanding without your knowledge.")
					.define("eggOnlyHatchesWhenOnTopOfHayBlock", true);			
			eggWillHatchChance = builder
					.comment("The chance an egg will hatch just before despawning if the entity limiter is not active.")
					.defineInRange("eggWillHatchChance", 1.0, 0, 1.0);
			onlyHatchIfLessChickensAroundThan = builder
					.comment("Prevents too many entities from hatching. A despawning egg will only hatch if there are less chickens than defined here in a radius of 'radiusEntityLimiterCheck' blocks around.")
					.defineInRange("onlyHatchIfLessChickensAroundThan", 50, 0, 1000);
			radiusEntityLimiterCheck = builder
					.comment("The radius around the despawned egg for 'onlyHatchIfLessChickensAroundThan'.")
					.defineInRange("radiusEntityLimiterCheck", 32, 1, 256);
			newHatchlingIsBaby = builder
					.comment("If enabled, the newly hatched chicken is a small chick.")
					.define("newHatchlingIsBaby", true);
			
			builder.pop();
		}
	}
}