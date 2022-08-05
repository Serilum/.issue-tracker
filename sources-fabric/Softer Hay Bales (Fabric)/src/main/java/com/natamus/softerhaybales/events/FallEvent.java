/*
 * This is the latest source code of Softer Hay Bales.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.softerhaybales.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class FallEvent {
	public static int onFall(Level world, Entity entity, float f, float g) {
		if (world.isClientSide) {
			return 1;
		}
		
		if (entity instanceof Player == false) {
			return 1;
		}
		
		BlockPos fallpos = entity.blockPosition().below();
		Block block = world.getBlockState(fallpos).getBlock();
		if (block.equals(Blocks.HAY_BLOCK)) {
			return 0;
		}
		
		return 1;
	}
}
