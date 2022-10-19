/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.10.
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

package com.natamus.collective_fabric.fakeplayer;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;

/*
 * Minecraft Forge
 * Copyright (c) 2016-2019.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ServerLevel;

//To be expanded for generic Mod fake players?
public class FakePlayerFactory
{
	private static final GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
	// Map of all active fake player usernames to their entities
	private static final Map<GameProfile, FakePlayer> fakePlayers = Maps.newHashMap();
	private static WeakReference<FakePlayer> MINECRAFT_PLAYER = null;

	public static FakePlayer getMinecraft(ServerLevel world)
	{
		FakePlayer ret = MINECRAFT_PLAYER != null ? MINECRAFT_PLAYER.get() : null;
		if (ret == null)
		{
			ret = FakePlayerFactory.get(world,  MINECRAFT);
			MINECRAFT_PLAYER = new WeakReference<>(ret);
		}
		return ret;
	}

	/**
	 * Get a fake player with a given username,
	 * Mods should either hold weak references to the return value, or listen for a
	 * WorldEvent.Unload and kill all references to prevent worlds staying in memory.
	 */
	public static FakePlayer get(ServerLevel world, GameProfile username)
	{
		if (!fakePlayers.containsKey(username))
		{
			FakePlayer fakePlayer = new FakePlayer(null, world, username, null);
			fakePlayers.put(username, fakePlayer);
		}

		return fakePlayers.get(username);
	}

	public static void unloadWorld(ServerLevel world)
	{
		fakePlayers.entrySet().removeIf(entry -> entry.getValue().getCommandSenderWorld() == world);
		if (MINECRAFT_PLAYER != null && MINECRAFT_PLAYER.get() != null && MINECRAFT_PLAYER.get().getCommandSenderWorld() == world) // This shouldn't be strictly necessary, but lets be aggressive.
		{
			FakePlayer mc = MINECRAFT_PLAYER.get();
			if (mc != null && mc.getCommandSenderWorld() == world)
			{
				MINECRAFT_PLAYER = null;
			}
		}
	}
}