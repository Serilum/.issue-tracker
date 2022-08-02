/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.automaticdoors.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.natamus.thevanillaexperience.mods.automaticdoors.config.AutomaticDoorsConfigHandler;
import com.natamus.thevanillaexperience.mods.automaticdoors.util.AutomaticDoorsUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AutomaticDoorsDoorEvent {	
	public static List<BlockPos> toclosedoors = new ArrayList<BlockPos>();
	public static List<BlockPos> newclosedoors = new ArrayList<BlockPos>();
	
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
				
		if (newclosedoors.size() > 0) {
			toclosedoors.addAll(newclosedoors);
			newclosedoors = new ArrayList<BlockPos>();
		}
		
		BlockPos ppos = player.blockPosition().above().immutable();
		if (toclosedoors.size() > 0) {
			List<BlockPos> closetoremove = new ArrayList<BlockPos>();
			
			for (BlockPos bp : toclosedoors) {
				if (bp == null) {
					closetoremove.add(bp);
					continue;
				}
				BlockState state = world.getBlockState(bp);
				Block block = state.getBlock();
				if (!AutomaticDoorsUtil.isDoor(block)) {
					closetoremove.add(bp);
					continue;
				}
								
				if (!ppos.closerThan(bp, 3) || (AutomaticDoorsConfigHandler.GENERAL.preventOpeningOnSneak.get() && player.isShiftKeyDown())) {
					Iterator<BlockPos> it0 = BlockPos.betweenClosed(bp.getX()-1, bp.getY(), bp.getZ()-1, bp.getX()+1, bp.getY(), bp.getZ()+1).iterator();
					while (it0.hasNext()) {
						BlockPos aroundpos = it0.next();
						BlockState aroundstate = world.getBlockState(aroundpos);
						Block aroundblock = aroundstate.getBlock();
						if (AutomaticDoorsUtil.isDoor(aroundblock)) {
							((DoorBlock)block).setOpen(player, world, aroundstate, aroundpos, false); // toggleDoor
						}
					}
					
					closetoremove.add(bp);
				}
			}
			
			if (closetoremove.size() > 0) {
				for (BlockPos tr : closetoremove) {
					toclosedoors.remove(tr);
				}
			}
		}
		
		if (player.isShiftKeyDown()) {
			if (AutomaticDoorsConfigHandler.GENERAL.preventOpeningOnSneak.get()) {
				return;
			}
		}
		
		Iterator<BlockPos> it1 = BlockPos.betweenClosedStream(ppos.getX()-1, ppos.getY(), ppos.getZ()-1, ppos.getX()+1, ppos.getY(), ppos.getZ()+1).iterator();
		while (it1.hasNext()) {
			BlockPos np = it1.next();
			BlockState state = world.getBlockState(np);
			Block block = state.getBlock();
			if (AutomaticDoorsUtil.isDoor(block)) {
				if (toclosedoors.contains(np) || newclosedoors.contains(np)) {
					continue;
				}
				
				((DoorBlock)block).setOpen(player, world, state, np, true); // toggleDoor
				AutomaticDoorsUtil.delayDoorClose(np.immutable());
			}
		}
	}
}
