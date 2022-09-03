/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.fallthroughslime.events;

import java.util.HashMap;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FallThroughSlimeSlimeEvent {
	private static HashMap<String, Vec3> lastvecs = new HashMap<String, Vec3>();
	private static HashMap<String, Integer> lastticks = new HashMap<String, Integer>();
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		String playername = player.getName().getString();
		if (!lastticks.containsKey(playername)) {
			lastticks.put(playername, 20);
			return;
		}
		
		int ticki = lastticks.get(playername);
		if (ticki > 0) {
			lastticks.put(playername, ticki-1);
			return;
		}
		lastticks.put(playername, 20);
		
		BlockPos ppos = player.blockPosition();
		Vec3 pvec = player.position();
		
		Vec3 lastvec = lastvecs.get(playername);
		lastvecs.put(playername, pvec);
		if (lastvec != null) {
			if (lastvec.x != pvec.x && lastvec.z != pvec.z) {
				return;
			}
		}
		else {
			return;
		}
		
		Block down = world.getBlockState(ppos.below()).getBlock();
		if (down.equals(Blocks.SLIME_BLOCK)) {
			player.teleportTo(pvec.x, pvec.y-0.2, pvec.z);
		}
	}
}
