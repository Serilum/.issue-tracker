/*
 * This is the latest source code of Double Doors.
 * Minecraft version: 1.18.1, mod version: 3.2.
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

package com.natamus.doubledoors.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.doubledoors.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Material;

public class Util {
	public static boolean isDoorBlock(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock) {
			return true;
		}
		return false;
	}
	
	public static boolean isPressureBlock(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (block instanceof PressurePlateBlock || block instanceof WoodButtonBlock || block instanceof StoneButtonBlock) {
			if (blockstate.getValue(BlockStateProperties.POWERED)) {
				return true;
			}
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
		if (block instanceof DoorBlock == false) {
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
				if (!ConfigHandler.GENERAL.enableDoors.get()) {
					continue;
				}
				
				DoorBlock door = (DoorBlock)oblock;
				
				if (playsound) {
					door.setOpen(player, world, ostate, toopen, isopen); // toggleDoor
					playsound = false;
				}
				else {
					world.setBlock(toopen, ostate.setValue(DoorBlock.OPEN, Boolean.valueOf(isopen)), 10);
				}
			}
			else if (block instanceof TrapDoorBlock) {
				if (!ConfigHandler.GENERAL.enableTrapdoors.get()) {
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

				world.setBlock(toopen, ostate.setValue(BlockStateProperties.OPEN, Boolean.valueOf(isopen)), 10);
			}
			else if (block instanceof FenceGateBlock) {
				if (!ConfigHandler.GENERAL.enableFenceGates.get()) {
					continue;
				}
				
				world.setBlock(toopen, ostate.setValue(DoorBlock.OPEN, Boolean.valueOf(isopen)), 10);
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
			
			if (!BlockPosFunctions.withinDistance(originalpos, bpa, ConfigHandler.GENERAL.recursiveOpeningMaxBlocksDistance.get())) {
				continue;
			}
			
			BlockState ostate = world.getBlockState(bpa);
			Block oblock = ostate.getBlock();
			if (Util.isDoorBlock(ostate)) {
				if (oblock.getRegistryName().equals(block.getRegistryName())) {
					postoopen.add(bpa.immutable());
					
					if (ConfigHandler.GENERAL.enableRecursiveOpening.get()) {
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
