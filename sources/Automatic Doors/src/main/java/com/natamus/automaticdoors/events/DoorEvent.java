/*
 * This is the latest source code of Automatic Doors.
 * Minecraft version: 1.19.2, mod version: 2.6.
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

package com.natamus.automaticdoors.events;

import com.natamus.automaticdoors.config.ConfigHandler;
import com.natamus.automaticdoors.util.Util;
import com.natamus.collective.functions.WorldFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber
public class DoorEvent {	
	public static HashMap<Level, List<BlockPos>> toclosedoors = new HashMap<Level, List<BlockPos>>();
	public static HashMap<Level, List<BlockPos>> newclosedoors = new HashMap<Level, List<BlockPos>>();

	@SubscribeEvent
	public void onWorldLoad(LevelEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}

		toclosedoors.put(world, new ArrayList<BlockPos>());
		newclosedoors.put(world, new ArrayList<BlockPos>());
	}

	@SubscribeEvent
	public void onWorldTick(LevelTickEvent e) {
		Level world = e.level;
		if (world.isClientSide || e.phase != Phase.START) {
			return;
		}

		if (newclosedoors.size() > 0) {
			toclosedoors.get(world).addAll(newclosedoors.get(world));
			newclosedoors.get(world).clear();
		}

		if (toclosedoors.size() > 0) {
			List<BlockPos> closetoremove = new ArrayList<BlockPos>();

			for (BlockPos bp : toclosedoors.get(world)) {
				if (bp == null) {
					closetoremove.add(bp);
					continue;
				}

				BlockState state = world.getBlockState(bp);
				Block block = state.getBlock();
				if (!Util.isDoor(block)) {
					closetoremove.add(bp);
					continue;
				}

				boolean canclose = true;
				for (Player player : world.getEntitiesOfClass(Player.class, new AABB(bp.getX() - 2, bp.getY(), bp.getZ() - 2, bp.getX() + 2, bp.getY(), bp.getZ() + 2))) {
					BlockPos ppos = player.blockPosition();

					if (ppos.closerThan(bp, 3)) {
						if (ConfigHandler.GENERAL.preventOpeningOnSneak.get() && player.isCrouching()) {
							continue;
						}

						canclose = false;
						break;
					}
				}

				if (canclose) {
					for (BlockPos aroundpos : BlockPos.betweenClosed(bp.getX() - 1, bp.getY(), bp.getZ() - 1, bp.getX() + 1, bp.getY(), bp.getZ() + 1)) {
						BlockState aroundstate = world.getBlockState(aroundpos);
						Block aroundblock = aroundstate.getBlock();
						if (Util.isDoor(aroundblock)) {
							((DoorBlock) block).setOpen(null, world, aroundstate, aroundpos, false); // toggleDoor
						}
					}

					closetoremove.add(bp);
				}
			}

			if (closetoremove.size() > 0) {
				for (BlockPos tr : closetoremove) {
					toclosedoors.get(world).remove(tr);
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (player.isSpectator()) {
			return;
		}

		if (player.isShiftKeyDown()) {
			if (ConfigHandler.GENERAL.preventOpeningOnSneak.get()) {
				return;
			}
		}

		BlockPos ppos = player.blockPosition().above().immutable();
		Iterator<BlockPos> it1 = BlockPos.betweenClosedStream(ppos.getX()-1, ppos.getY(), ppos.getZ()-1, ppos.getX()+1, ppos.getY(), ppos.getZ()+1).iterator();
		while (it1.hasNext()) {
			BlockPos np = it1.next();
			BlockState state = world.getBlockState(np);
			Block block = state.getBlock();
			if (Util.isDoor(block)) {
				if (toclosedoors.get(world).contains(np) || newclosedoors.get(world).contains(np)) {
					continue;
				}
				
				((DoorBlock)block).setOpen(player, world, state, np, true); // toggleDoor
				Util.delayDoorClose(world, np.immutable());
			}
		}
	}
}
