/*
 * This is the latest source code of Edibles.
 * Minecraft version: 1.19.2, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.edibles.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	public static final Weakness WEAKNESS = new Weakness(BUILDER);
	public static final Glow GLOW = new Glow(BUILDER);
	public static final Other OTHER = new Other(BUILDER);
	
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class Weakness {
		public final ForgeConfigSpec.ConfigValue<Integer> maxItemUsesPerDaySingleItem;
		public final ForgeConfigSpec.ConfigValue<Integer> maxItemUsesPerDayTotal;
		public final ForgeConfigSpec.ConfigValue<Integer> weaknessDurationSeconds;
		
		public Weakness(ForgeConfigSpec.Builder builder) {
			builder.push("Weakness Effect");
			maxItemUsesPerDaySingleItem = builder
					.comment("The maximum amount of an item a player can eat before receiving the weakness effect. A value of -1 disables this feature.")
					.defineInRange("maxItemUsesPerDaySingleItem", 16, -1, 1280);
			maxItemUsesPerDayTotal = builder
					.comment("The maximum of the total amount of items a player can eat before receiving the weakness effect. A value of -1 disables this feature.")
					.defineInRange("maxItemUsesPerDayTotal", -1, -1, 1280);
			weaknessDurationSeconds = builder
					.comment("The duration of the weakness effect in seconds when eating too much of an item.")
					.defineInRange("weaknessDurationSeconds", 45, 1, 3600);
			
			builder.pop();
		}
	}
		
	public static class Glow {
		public final ForgeConfigSpec.ConfigValue<Integer> glowEntityDurationSeconds;
		public final ForgeConfigSpec.ConfigValue<Integer> glowEntitiesAroundAffectedRadiusBlocks;
		
		public Glow(ForgeConfigSpec.Builder builder) {
			builder.push("Glow Effect");
			glowEntityDurationSeconds = builder
					.comment("When eating glowstone, the duration in seconds of how long entities around should be glowing with an outline. A value of 0 disables the item use.")
					.defineInRange("glowEntityDurationSeconds", 20, 0, 3600);
			glowEntitiesAroundAffectedRadiusBlocks = builder
					.comment("For the glow effect, the radius in blocks around the player of entities affected.")
					.defineInRange("glowEntitiesAroundAffectedRadiusBlocks", 32, 1, 128);
			
			builder.pop();
		}
	}
			
	public static class Other {
		public final ForgeConfigSpec.ConfigValue<Integer> _cooldownInMsBetweenUses;
		public final ForgeConfigSpec.ConfigValue<Integer> blazePowderStrengthDurationSeconds;
		public final ForgeConfigSpec.ConfigValue<Integer> magmaCreamFireResistanceDurationSeconds;
		public final ForgeConfigSpec.ConfigValue<Integer> sugarSpeedDurationSeconds;
		public final ForgeConfigSpec.ConfigValue<Integer> ghastTearDurationSeconds;
		public final ForgeConfigSpec.ConfigValue<Integer> phantomMembraneDurationSeconds;
		public final ForgeConfigSpec.ConfigValue<Integer> rabbitsFootDurationSeconds;
		
		public Other(ForgeConfigSpec.Builder builder) {
			builder.push("Other Effects");
			_cooldownInMsBetweenUses = builder
					.comment("The time in miliseconds of cooldown in between uses of eating an edible.")
					.defineInRange("_cooldownInMsBetweenUses", 1000, 0, 3600000);
			blazePowderStrengthDurationSeconds = builder
					.comment("After eating blaze powder, the duration in seconds of the strength effect the player receives. A value of 0 disables the item use.")
					.defineInRange("blazePowderStrengthDurationSeconds", 15, 0, 3600);
			magmaCreamFireResistanceDurationSeconds = builder
					.comment("After eating magma cream, the duration in seconds of the fire resistance effect the player receives. A value of 0 disables the item use.")
					.defineInRange("magmaCreamFireResistanceDurationSeconds", 15, 0, 3600);
			sugarSpeedDurationSeconds = builder
					.comment("After eating some sugar, the duration in seconds of the speed effect the player receives. A value of 0 disables the item use.")
					.defineInRange("sugarSpeedDurationSeconds", 15, 0, 3600);
			ghastTearDurationSeconds = builder
					.comment("After eating a ghast tear, the duration in seconds of the regeneration effect the player receives. A value of 0 disables the item use.")
					.defineInRange("ghastTearDurationSeconds", 15, 0, 3600);
			phantomMembraneDurationSeconds = builder
					.comment("After eating some phantom membrane, the duration in seconds of the slow falling effect the player receives. A value of 0 disables the item use.")
					.defineInRange("phantomMembraneDurationSeconds", 15, 0, 3600);
			rabbitsFootDurationSeconds = builder
					.comment("After eating a rabbit's foot, the duration in seconds of the jump boost effect the player receives. A value of 0 disables the item use.")
					.defineInRange("rabbitsFootDurationSeconds", 15, 0, 3600);
			
			builder.pop();
		}
	}
}