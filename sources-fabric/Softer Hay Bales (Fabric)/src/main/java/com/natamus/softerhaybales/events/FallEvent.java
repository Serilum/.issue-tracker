/*
 * This is the latest source code of Softer Hay Bales.
 * Minecraft version: 1.19.3, mod version: 2.3.
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
