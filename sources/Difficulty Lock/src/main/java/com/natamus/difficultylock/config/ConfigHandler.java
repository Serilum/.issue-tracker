/*
 * This is the latest source code of Difficulty Lock.
 * Minecraft version: 1.19.3, mod version: 1.6.
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

package com.natamus.difficultylock.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> forcePeaceful;
		public final ForgeConfigSpec.ConfigValue<Boolean> forceEasy;
		public final ForgeConfigSpec.ConfigValue<Boolean> forceNormal;
		public final ForgeConfigSpec.ConfigValue<Boolean> forceHard;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> shouldLockDifficulty;
		public final ForgeConfigSpec.ConfigValue<Boolean> shouldChangeDifficultyWhenAlreadyLocked;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			forcePeaceful = builder
					.comment("Priority 1: Sets the difficulty in any world to peaceful when enabled.")
					.define("forcePeaceful", false);
			forceEasy = builder
					.comment("Priority 2: Sets the difficulty in any world to easy when enabled.")
					.define("forceEasy", false);
			forceNormal = builder
					.comment("Priority 3: Sets the difficulty in any world to normal when enabled.")
					.define("forceNormal", false);
			forceHard = builder
					.comment("Priority 4: Sets the difficulty in any world to hard when enabled.")
					.define("forceHard", true);
			
			shouldLockDifficulty = builder
					.comment("When enabled, locks the difficulty in any world so it cannot be changed.")
					.define("shouldLockDifficulty", true);
			shouldChangeDifficultyWhenAlreadyLocked = builder
					.comment("When enabled, also sets the difficulty in worlds where it has already been locked.")
					.define("shouldChangeDifficultyWhenAlreadyLocked", false);
			
			builder.pop();
		}
	}
}