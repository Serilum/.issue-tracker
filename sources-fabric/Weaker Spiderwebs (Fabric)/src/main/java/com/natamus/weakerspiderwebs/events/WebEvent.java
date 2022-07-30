/*
 * This is the latest source code of Weaker Spiderwebs.
 * Minecraft version: 1.19.1, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Weaker Spiderwebs ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.weakerspiderwebs.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.natamus.weakerspiderwebs.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.phys.Vec3;

public class WebEvent {
	private static Map<String, List<BlockPos>> todestroy = new HashMap<String, List<BlockPos>>();
	
	public static void onPlayerTick(ServerLevel world, ServerPlayer player) {
		String playername = player.getName().getString();
		
		if (todestroy.get(playername) == null) {
			todestroy.put(playername, new ArrayList<BlockPos>());
		}
		else if (todestroy.get(playername).size() > 0) {
			List<BlockPos> tdcopy = Collections.unmodifiableList(todestroy.get(playername));
			todestroy.put(playername, new ArrayList<BlockPos>());
			for (BlockPos td : tdcopy) {
				try {
					world.destroyBlock(td, true);
				}
				catch (NullPointerException ex) {
					continue;
				}
			}
		}
		
		if (player.tickCount % 5 != 0) {
			return;
		}
		
		Vec3 pvec = player.position();
		int ypos = (int)Math.ceil(pvec.y);
		BlockPos pos = new BlockPos(pvec.x, ypos, pvec.z);
		
		if (world.getBlockState(pos.below()).getBlock() instanceof WebBlock || world.getBlockState(pos).getBlock() instanceof WebBlock || world.getBlockState(pos.above()).getBlock() instanceof WebBlock) {
			new Thread( new Runnable() {
		    	public void run()  {
		        	try  { Thread.sleep( ConfigHandler.breakSpiderwebDelay.getValue() ); }
		            catch (InterruptedException ie)  {}
		        	
		        	BlockPos nowpos = player.blockPosition().immutable();
		        	if (pos.getX() != nowpos.getX() || pos.getZ() != nowpos.getZ()) {
		        		return;
		        	}
		        	if (world.getBlockState(pos.below()).getBlock() instanceof WebBlock) {
		        		todestroy.get(playername).add(pos.below().immutable());
		        	}
		        	if (world.getBlockState(pos).getBlock() instanceof WebBlock) {
		        		todestroy.get(playername).add(pos.immutable());
		        	}
		        	if (world.getBlockState(pos.above()).getBlock() instanceof WebBlock) {
		        		todestroy.get(playername).add(pos.above().immutable());
		        	}
		    	}
		    } ).start();
		}
	}
}
