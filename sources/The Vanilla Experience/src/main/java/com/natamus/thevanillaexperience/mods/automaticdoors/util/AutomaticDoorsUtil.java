/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.automaticdoors.util;

import java.util.ArrayList;
import java.util.List;

import com.natamus.thevanillaexperience.mods.automaticdoors.config.AutomaticDoorsConfigHandler;
import com.natamus.thevanillaexperience.mods.automaticdoors.events.AutomaticDoorsDoorEvent;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.core.BlockPos;

public class AutomaticDoorsUtil {
	private static List<BlockPos> runnables = new ArrayList<BlockPos>();
	
	public static Boolean isDoor(Block block) {
		if (block instanceof DoorBlock) {
			if (!AutomaticDoorsConfigHandler.GENERAL.shouldOpenIronDoors.get()) {
				String name = block.toString().toLowerCase();
				if (name.contains("iron")) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static void delayDoorClose(BlockPos pos) {
		if (pos == null) {
			return;
		}
		
		if (runnables.contains(pos)) {
			return;
		}
		
		runnables.add(pos);
		new Thread( new Runnable() {
	    	public void run()  {
	        	try  { Thread.sleep( AutomaticDoorsConfigHandler.GENERAL.doorOpenTime.get() ); }
	            catch (InterruptedException ie)  {}
	        	
	        	if (!AutomaticDoorsDoorEvent.toclosedoors.contains(pos) && !AutomaticDoorsDoorEvent.newclosedoors.contains(pos)) {
	        		AutomaticDoorsDoorEvent.newclosedoors.add(pos);
	        	}
	        	runnables.remove(pos);
	    	}
	    } ).start();
	}
}
