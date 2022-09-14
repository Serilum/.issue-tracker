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

package com.natamus.thevanillaexperience.mods.mooshroomspawn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MooshroomSpawnConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Double> chanceCowIsMooshroom;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			chanceCowIsMooshroom = builder
					.comment("The chance a cow that has spawned is of the mooshroom variant.")
					.defineInRange("chanceCowIsMooshroom", 0.05, 0, 1.0);
			
			builder.pop();
		}
	}
}