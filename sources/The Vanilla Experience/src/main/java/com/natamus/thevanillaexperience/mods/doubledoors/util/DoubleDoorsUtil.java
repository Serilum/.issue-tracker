/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.doubledoors.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DoubleDoorsUtil {
	public static boolean isDoorBlock(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock) {
		//if (blockstate.has(BlockStateProperties.OPEN)) {
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
}
