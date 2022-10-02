/*
 * This is the latest source code of Trample Everything.
 * Minecraft version: 1.19.2, mod version: 2.0.
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

package com.natamus.trampleeverything.events;

import com.natamus.trampleeverything.config.ConfigHandler;
import com.natamus.trampleeverything.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

public class TrampleEvent {
	public static void onPlayerTick(ServerLevel world, ServerPlayer player) {
		if (player.isCrouching()) {
			if (ConfigHandler._crouchingPreventsTrampling) {
				return;
			}
		}
		
		BlockPos playerpos = player.blockPosition();
		Block block = world.getBlockState(playerpos).getBlock();

		Util.trampleCheck(world, playerpos, block);
	}
}
