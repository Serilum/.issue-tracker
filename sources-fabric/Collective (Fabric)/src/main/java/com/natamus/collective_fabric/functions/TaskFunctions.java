/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.15.
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
