/*
 * This is the latest source code of Weaker Spiderwebs.
 * Minecraft version: 1.19.2, mod version: 2.7.
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

package com.natamus.weakerspiderwebs.events;

import com.natamus.weakerspiderwebs.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.*;

@EventBusSubscriber
public class WebEvent {
	private static Map<String, List<BlockPos>> todestroy = new HashMap<String, List<BlockPos>>();
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.END)) {
			return;
		}

		if (player.isSpectator()) {
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
				catch (NullPointerException ignored) { }
			}
		}
		
		if (player.tickCount % 20 != 0) {
			return;
		}
		
		Vec3 pvec = player.position();
		int ypos = (int)Math.ceil(pvec.y);
		BlockPos pos = new BlockPos(pvec.x, ypos, pvec.z);
		
		if (world.getBlockState(pos.below()).getBlock() instanceof WebBlock || world.getBlockState(pos).getBlock() instanceof WebBlock || world.getBlockState(pos.above()).getBlock() instanceof WebBlock) {
			new Thread(() -> {
				try  { Thread.sleep( ConfigHandler.GENERAL.breakSpiderwebDelay.get() ); }
				catch (InterruptedException ignored)  {}

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
			}).start();
		}
	}
}
