/*
 * This is the latest source code of Automatic Doors.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.automaticdoors.util;

import java.util.ArrayList;
import java.util.List;

import com.natamus.automaticdoors.config.ConfigHandler;
import com.natamus.automaticdoors.events.DoorEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.core.BlockPos;

public class Util {
	private static List<BlockPos> runnables = new ArrayList<BlockPos>();
	
	public static Boolean isDoor(Block block) {
		if (block instanceof DoorBlock) {
			if (!ConfigHandler.GENERAL.shouldOpenIronDoors.get()) {
				String name = block.toString().toLowerCase();
				return !name.contains("iron");
			}
			return true;
		}
		return false;
	}
	
	public static void delayDoorClose(Level world, BlockPos pos) {
		if (pos == null) {
			return;
		}
		
		if (runnables.contains(pos)) {
			return;
		}
		
		runnables.add(pos);
		new Thread(() -> {
			try  { Thread.sleep( ConfigHandler.GENERAL.doorOpenTime.get() ); }
			catch (InterruptedException ignored)  {}

			if (!DoorEvent.toclosedoors.get(world).contains(pos) && !DoorEvent.newclosedoors.get(world).contains(pos)) {
				DoorEvent.newclosedoors.get(world).add(pos);
			}
			runnables.remove(pos);
		}).start();
	}
}
