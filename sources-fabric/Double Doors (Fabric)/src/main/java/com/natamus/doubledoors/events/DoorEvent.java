/*
 * This is the latest source code of Double Doors.
 * Minecraft version: 1.18.x, mod version: 3.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Double Doors ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.doubledoors.events;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.natamus.doubledoors.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

public class DoorEvent {
	private static List<BlockPos> prevpoweredpos = new ArrayList<BlockPos>();
	private static HashMap<BlockPos, Integer> prevbuttonpos = new HashMap<BlockPos, Integer>();
	
	public static void onNeighbourNotice(Level world, BlockPos pos, BlockState state, EnumSet<Direction> notifiedSides, boolean forceRedstoneUpdate) {
		if (world.isClientSide) {
			return;
		}
		
		BooleanProperty proppowered = BlockStateProperties.POWERED;
		Block block = state.getBlock();
		
		if (block instanceof PressurePlateBlock == false) {
			if (block instanceof StoneButtonBlock == false && block instanceof WoodButtonBlock == false) {
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
		else {
			if (!state.getValue(proppowered)) {
				if (!prevpoweredpos.contains(pos)) {
					return;
				}
			}
		}
		
		boolean playsound = true;
		boolean stateprop = state.getValue(proppowered);
		
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