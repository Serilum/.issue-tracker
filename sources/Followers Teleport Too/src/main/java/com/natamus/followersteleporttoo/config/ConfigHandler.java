/*
 * This is the latest source code of Followers Teleport Too.
 * Minecraft version: 1.19.3, mod version: 1.1.
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

package com.natamus.followersteleporttoo.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> disableFollowerDamageAfterTeleport;
		public final ForgeConfigSpec.ConfigValue<Integer> durationInSecondsDamageShouldBeDisabled;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			disableFollowerDamageAfterTeleport = builder
					.comment("When enabled, disables damage for followers shortly after a teleport. This can prevent fall damage or suffocation from an estimate target position.")
					.define("disableFollowerDamageAfterTeleport", true);

			durationInSecondsDamageShouldBeDisabled = builder
					.comment("How long in seconds damage should be disabled for after a teleport when disableFollowerDamageAfterTeleport is enabled.")
					.defineInRange("durationInSecondsDamageShouldBeDisabled", 20, 0, 86400);
			
			builder.pop();
		}
	}
}