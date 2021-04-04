/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.mods.weakerspiderwebs.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.natamus.thevanillaexperience.mods.weakerspiderwebs.config.WeakerSpiderwebsConfigHandler;

import net.minecraft.block.WebBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WeakerSpiderwebsWebEvent {
	private static Map<String, List<BlockPos>> todestroy = new HashMap<String, List<BlockPos>>();
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		PlayerEntity player = e.player;
		World world = player.getEntityWorld();
		if (world.isRemote || !e.phase.equals(Phase.END)) {
			return;
		}
		
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
		
		if (player.ticksExisted % 20 != 0) {
			return;
		}
		
		BlockPos pos = player.getPosition().toImmutable();
		if (world.getBlockState(pos).getBlock() instanceof WebBlock || world.getBlockState(pos.up()).getBlock() instanceof WebBlock) {
			new Thread( new Runnable() {
		    	public void run()  {
		        	try  { Thread.sleep( WeakerSpiderwebsConfigHandler.GENERAL.breakSpiderwebDelay.get() ); }
		            catch (InterruptedException ie)  {}
		        	
		        	BlockPos nowpos = player.getPosition().toImmutable();
		        	if (pos.getX() != nowpos.getX() || pos.getZ() != nowpos.getZ()) {
		        		return;
		        	}
		        	if (world.getBlockState(pos).getBlock() instanceof WebBlock) {
		        		todestroy.get(playername).add(pos.toImmutable());
		        	}
		        	if (world.getBlockState(pos.up()).getBlock() instanceof WebBlock) {
		        		todestroy.get(playername).add(pos.up().toImmutable());
		        	}
		    	}
		    } ).start();
		}
	}
}
