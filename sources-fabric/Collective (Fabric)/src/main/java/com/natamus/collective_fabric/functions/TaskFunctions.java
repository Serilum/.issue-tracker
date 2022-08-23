/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.41.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
