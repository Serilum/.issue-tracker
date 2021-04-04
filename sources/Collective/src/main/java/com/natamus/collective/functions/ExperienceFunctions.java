/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.16.5, mod version: 2.25.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityStatusPacket;

public class ExperienceFunctions {
	public static boolean canConsumeXp(final PlayerEntity ep, final int xp) {
		if (ep.isCreative()) {
			return true;
		}
		return xp <= 0 || getPlayerXP(ep) >= xp;
	}

	public static void consumeXp(final PlayerEntity ep, final int xp) {
		if (xp <= 0) {
			return;
		}

		final int playerXP = getPlayerXP(ep);
		if (playerXP >= xp) {
			addPlayerXP(ep, -xp);
		}

		if (ep instanceof ServerPlayerEntity) {
			((ServerPlayerEntity) ep).connection.sendPacket(new SEntityStatusPacket(ep, (byte) 9));
		}
	}

	public static int getPlayerXP(final PlayerEntity player) {
		return (int) (getExperienceForLevel(player.experienceLevel) + player.experience * player.xpBarCap());
	}

	public static void addPlayerXP(final PlayerEntity player, final int amount) {
		final int experience = getPlayerXP(player) + amount;
		player.experienceTotal = experience;
		player.experienceLevel = getLevelForExperience(experience);
		final int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
	}

	private static int sum(final int n, final int a0, final int d) {
		return n * (2 * a0 + (n - 1) * d) / 2;
	}

	public static int getLevelForExperience(int targetXp) {
		int level = 0;
		while (true) {
			final int xpToNextLevel = xpBarCap(level);
			if (targetXp < xpToNextLevel) {
				return level;
			}
			level++;
			targetXp -= xpToNextLevel;
		}
	}

	public static int xpBarCap(final int level) {
		if (level >= 30) {
			return 112 + (level - 30) * 9;
		}

		if (level >= 15) {
			return 37 + (level - 15) * 5;
		}

		return 7 + level * 2;
	}

	public static int getExperienceForLevel(final int level) {
		if (level == 0) {
			return 0;
		}
		if (level <= 15) {
			return sum(level, 7, 2);
		}
		if (level <= 30) {
			return 315 + sum(level - 15, 37, 5);
		}
		return 1395 + sum(level - 30, 112, 9);
	}
}