/*
 * This is the latest source code of Random Mob Effects.
 * Minecraft version: 1.19.3, mod version: 2.4.
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

package com.natamus.randommobeffects.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> potionEffectLevel;
		public final ForgeConfigSpec.ConfigValue<Boolean> hideEffectParticles;
		public final ForgeConfigSpec.ConfigValue<Boolean> disableCreepers;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			potionEffectLevel = builder
					.comment("The default level of the effects the mod applies, by default 1.")
					.defineInRange("potionEffectLevel", 1, 1, 50);
			hideEffectParticles = builder
					.comment("When enabled, hides the particles from the mobs with an effect.")
					.define("hideEffectParticles", false);
			disableCreepers = builder
					.comment("Creepers can create infinite lingering potion effects which is probably not what you want. When enabled, the mod doesn't give creepers a random effect.")
					.define("disableCreepers", true);
			
			builder.pop();
		}
	}
}