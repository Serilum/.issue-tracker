/*
 * This is the latest source code of Double Doors.
 * Minecraft version: 1.19.2, mod version: 3.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.doubledoors.events;

import com.natamus.doubledoors.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import java.util.*;

public class DoorEvent {
	private static List<BlockPos> prevpoweredpos = new ArrayList<BlockPos>();
	private static HashMap<BlockPos, Integer> prevbuttonpos = new HashMap<BlockPos, Integer>();
	
	public static void onNeighbourNotice(Level world, BlockPos pos, BlockState state, EnumSet<Direction> notifiedSides, boolean forceRedstoneUpdate) {
		if (world.isClientSide) {
			return;
		}
		
		BooleanProperty proppowered = BlockStateProperties.POWERED;
		IntegerProperty weightedpower = BlockStateProperties.POWER;
		pos = pos.immutable();
		Block block = state.getBlock();

		if (!(block instanceof PressurePlateBlock) && !(block instanceof WeightedPressurePlateBlock)) {
			if (!(block instanceof StoneButtonBlock) && !(block instanceof WoodButtonBlock)) {
				return;
			}
			else {
				if (prevbuttonpos.containsKey(pos)) {
					prevbuttonpos.remove(pos);
				}
				else {
					prevbuttonpos.put(pos, 1);
					return;
				}

				if (!state.getValue(proppowered)) {
					if (!prevpoweredpos.contains(pos)) {
						return;
					}
					prevpoweredpos.remove(pos);
				}
			}
		}
		else if (block instanceof WeightedPressurePlateBlock) {
			if (state.getValue(weightedpower) == 0) {
				if (!prevpoweredpos.contains(pos)) {
					return;
				}
			}
		}
		else {
			if (!state.getValue(proppowered)) {
				if (!prevpoweredpos.contains(pos)) {
					return;
				}
			}
		}

		boolean playsound = true;
		boolean stateprop;
		if (block instanceof WeightedPressurePlateBlock) {
			stateprop = state.getValue(weightedpower) > 0;
		}
		else {
			stateprop = state.getValue(proppowered);
		}

		Iterator<BlockPos> blocksaround = BlockPos.betweenClosedStream(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1).iterator();

		BlockPos doorpos = null;
		while (blocksaround.hasNext()) {
			BlockPos npos = blocksaround.next().immutable();
			BlockState ostate = world.getBlockState(npos);
			if (Util.isDoorBlock(ostate)) {
				doorpos = npos;
				break;
			}
		}

		if (doorpos != null) {
			if (Util.processDoor(null, world, doorpos, world.getBlockState(doorpos), stateprop, playsound)) {
				if (stateprop) {
					prevpoweredpos.add(pos);
				}
			}
		}
	}
	
	public static boolean onDoorClick(Level world, Player player, InteractionHand hand, BlockPos cpos, BlockHitResult hitVec) {
		if (world.isClientSide || !hand.equals(InteractionHand.MAIN_HAND)) {
			return true;
		}
		
		if (player.isShiftKeyDown()) {
			return true;
		}
		
		BlockState clickstate = world.getBlockState(cpos);

		if (!Util.isDoorBlock(clickstate)) {
			return true;
		}
		if (clickstate.getMaterial().equals(Material.METAL)) {
			return true;
		}

		if (Util.processDoor(player, world, cpos, clickstate, null, true)) {
			return false;
		}

		return true;
	}
}