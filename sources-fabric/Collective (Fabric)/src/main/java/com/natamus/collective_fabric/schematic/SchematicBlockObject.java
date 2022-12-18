/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.43.
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

package com.natamus.collective_fabric.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SchematicBlockObject {

	private BlockPos position;
	private BlockState state;
	
	public SchematicBlockObject(BlockPos position, BlockState state) {
		this.position = position;
		this.state = state;
	}
	
	public BlockPos getPosition() {
		return position;
	}
	
	public BlockState getState() {
		return state;
	}
	
	public BlockPos getPositionWithOfsset(int x, int y, int z) {
		return new BlockPos(x + position.getX(), y + position.getY(), z + position.getZ());
	}
}
