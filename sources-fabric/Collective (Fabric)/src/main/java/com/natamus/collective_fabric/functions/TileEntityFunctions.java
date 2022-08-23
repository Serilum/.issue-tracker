/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.41.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityFunctions {
	public static void updateTileEntity(Level world, BlockPos pos) {
		BlockEntity tileentity = world.getBlockEntity(pos); // CHECK FOR NULL
		updateTileEntity(world, pos, tileentity);
	}
	public static void updateTileEntity(Level world, BlockPos pos, BlockEntity tileentity) {
		BlockState state = world.getBlockState(pos);
		updateTileEntity(world, pos, state, tileentity);
	}
	public static void updateTileEntity(Level world, BlockPos pos, BlockState state, BlockEntity tileentity) {
		world.setBlocksDirty(pos, state, state);
		world.sendBlockUpdated(pos, state, state, 3);
		tileentity.setChanged();
	}
}
