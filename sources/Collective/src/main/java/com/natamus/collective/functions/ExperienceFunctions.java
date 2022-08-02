/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.30.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective.functions;

import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ExperienceFunctions {
	public static boolean canConsumeXp(final Player ep, final int xp) {
		if (ep.isCreative()) {
			return true;
		}
		return xp <= 0 || getPlayerXP(ep) >= xp;
	}

	public static void consumeXp(final Player ep, final int xp) {
		if (xp <= 0) {
			return;
		}

		final int playerXP = getPlayerXP(ep);
		if (playerXP >= xp) {
			addPlayerXP(ep, -xp);
		}

		if (ep instanceof ServerPlayer) {
			((ServerPlayer) ep).connection.send(new ClientboundEntityEventPacket(ep, (byte) 9));
		}
	}

	public static int getPlayerXP(final Player player) {
		return (int) (getExperienceForLevel(player.experienceLevel) + player.experienceProgress * player.getXpNeededForNextLevel());
	}

	public static void addPlayerXP(final Player player, final int amount) {
		final int experience = getPlayerXP(player) + amount;
		player.totalExperience = experience;
		player.experienceLevel = getLevelForExperience(experience);
		final int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experienceProgress = (float) (experience - expForLevel) / (float) player.getXpNeededForNextLevel();
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