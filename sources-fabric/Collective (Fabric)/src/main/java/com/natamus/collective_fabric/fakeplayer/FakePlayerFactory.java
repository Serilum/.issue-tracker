/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 3.8.
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
	private static GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
	// Map of all active fake player usernames to their entities
	private static Map<GameProfile, FakePlayer> fakePlayers = Maps.newHashMap();
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