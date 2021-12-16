/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 3.14.
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

package com.natamus.collective_fabric.functions;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class TaskFunctions {
	public static void enqueueImmediateTask(Level world, Runnable task, boolean allowClient) {
		if (world.isClientSide && allowClient) {
			task.run();
		}
		else {
			enqueueTask(world, task, 0);
		}
	}

	public static void enqueueTask(Level world, Runnable task, int delay) {
    	if (!(world instanceof ServerLevel)) {
    		return;
    	}

    	MinecraftServer server = ((ServerLevel)world).getServer();
    	server.submit(new TickTask(server.getTickCount() + delay, task));
	}
}
