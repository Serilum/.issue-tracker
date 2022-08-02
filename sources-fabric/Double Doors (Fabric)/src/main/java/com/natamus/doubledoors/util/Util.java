/*
 * This is the latest source code of Double Doors.
 * Minecraft version: 1.19.1, mod version: 3.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.doubledoors.util;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.doubledoors.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Util {
	public static boolean isDoorBlock(BlockState blockstate) {
		Block block = blockstate.getBlock();
		return block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock;
	}

	public static boolean isPressureBlock(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (block instanceof WeightedPressurePlateBlock) {
			return blockstate.getValue(BlockStateProperties.POWER) > 0;
		}
		if (block instanceof PressurePlateBlock || block instanceof WoodButtonBlock || block instanceof StoneButtonBlock) {
			return blockstate.getValue(BlockStateProperties.POWERED);
		}
		return false;
	}
	
	public static boolean processDoor(Player player, Level world, BlockPos pos, BlockState state, Boolean isopen, boolean playsound) {
		Block block = state.getBlock();
		if (block instanceof DoorBlock) {
			if (state.getValue(DoorBlock.HALF).equals(DoubleBlockHalf.UPPER)) {
				pos = pos.below().immutable();
				state = world.getBlockState(pos);
			}
		}
		
		if (isopen == null) {
			isopen = !state.getValue(BlockStateProperties.OPEN);
		}
		
		int yoffset = 0;
		if (!(block instanceof DoorBlock)) {
			yoffset = 1;
		}
		
		List<BlockPos> postoopen = recursivelyOpenDoors(new ArrayList<BlockPos>(Arrays.asList(pos.immutable())), new ArrayList<BlockPos>(), world, pos, pos, block, yoffset);
		if (postoopen.size() <= 1) {
			return false;
		}
		
		for (BlockPos toopen : postoopen) {
			BlockState ostate = world.getBlockState(toopen);
			Block oblock = ostate.getBlock();
			
			if (block instanceof DoorBlock) {
				if (!ConfigHandler.enableDoors.getValue()) {
					continue;
				}
				
				DoorBlock door = (DoorBlock)oblock;
				
				if (playsound) {
					door.setOpen(player, world, ostate, toopen, isopen); // toggleDoor
					playsound = false;
				}
				else {
					world.setBlock(toopen, ostate.setValue(DoorBlock.OPEN, isopen), 10);
				}
			}
			else if (block instanceof TrapDoorBlock) {
				if (!ConfigHandler.enableTrapdoors.getValue()) {
					continue;
				}
				
				if (playsound) {
					if (isopen) {
						int i = ostate.getMaterial() == Material.METAL ? 1037 : 1007;
						world.levelEvent(null, i, pos, 0);
					} else {
						int j = ostate.getMaterial() == Material.METAL ? 1036 : 1013;
						world.levelEvent(null, j, pos, 0);
					}
					playsound = false;
				}

				world.setBlock(toopen, ostate.setValue(BlockStateProperties.OPEN, isopen), 10);
			}
			else if (block instanceof FenceGateBlock) {
				if (!ConfigHandler.enableFenceGates.getValue()) {
					continue;
				}
				
				world.setBlock(toopen, ostate.setValue(DoorBlock.OPEN, isopen), 10);
			}
		}
		
		return postoopen.size() > 1;
	}
	
	private static List<BlockPos> recursivelyOpenDoors(List<BlockPos> postoopen, List<BlockPos> ignoreoopen, Level world, BlockPos originalpos, BlockPos pos, Block block, int yoffset) {
		Iterator<BlockPos> blocksaround = BlockPos.betweenClosedStream(pos.getX()-1, pos.getY()-yoffset, pos.getZ()-1, pos.getX()+1, pos.getY()+yoffset, pos.getZ()+1).iterator();
		while (blocksaround.hasNext()) {
			BlockPos bpa = blocksaround.next();
			if (postoopen.contains(bpa)) {
				continue;
			}
			
			if (!BlockPosFunctions.withinDistance(originalpos, bpa, ConfigHandler.recursiveOpeningMaxBlocksDistance.getValue())) {
				continue;
			}
			
			BlockState ostate = world.getBlockState(bpa);
			Block oblock = ostate.getBlock();
			if (Util.isDoorBlock(ostate)) {
				if (oblock.getName().equals(block.getName())) {
					postoopen.add(bpa.immutable());
					
					if (ConfigHandler.enableRecursiveOpening.getValue()) {
						recursivelyOpenDoors(postoopen, ignoreoopen, world, originalpos, bpa, block, yoffset);
					}
					continue;
				}
			}
			
			ignoreoopen.add(bpa.immutable());
		}
		
		return postoopen;
	}
}
