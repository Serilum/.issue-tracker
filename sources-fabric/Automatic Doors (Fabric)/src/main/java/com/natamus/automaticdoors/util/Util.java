/*
 * This is the latest source code of Automatic Doors.
 * Minecraft version: 1.19.1, mod version: 2.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Automatic Doors ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
			if (!ConfigHandler.shouldOpenIronDoors.getValue()) {
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
			try  { Thread.sleep( ConfigHandler.doorOpenTime.getValue() ); }
			catch (InterruptedException ignored)  {}

			if (!DoorEvent.toclosedoors.get(world).contains(pos) && !DoorEvent.newclosedoors.get(world).contains(pos)) {
				DoorEvent.newclosedoors.get(world).add(pos);
			}
			runnables.remove(pos);
		}).start();
	}
}
