/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.1, mod version: 3.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

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
