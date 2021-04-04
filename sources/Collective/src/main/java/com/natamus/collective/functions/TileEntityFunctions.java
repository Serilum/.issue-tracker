/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.16.5, mod version: 2.25.
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

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityFunctions {
	public static void updateTileEntity(World world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos); // CHECK FOR NULL
		updateTileEntity(world, pos, tileentity);
	}
	public static void updateTileEntity(World world, BlockPos pos, TileEntity tileentity) {
		BlockState state = world.getBlockState(pos);
		updateTileEntity(world, pos, state, tileentity);
	}
	public static void updateTileEntity(World world, BlockPos pos, BlockState state, TileEntity tileentity) {
		world.markBlockRangeForRenderUpdate(pos, state, state);
		world.notifyBlockUpdate(pos, state, state, 3);
		tileentity.markDirty();
	}
}
