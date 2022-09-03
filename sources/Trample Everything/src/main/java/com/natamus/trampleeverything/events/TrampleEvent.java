/*
 * This is the latest source code of Trample Everything.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.trampleeverything.events;

import com.natamus.trampleeverything.config.ConfigHandler;
import com.natamus.trampleeverything.util.Util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class TrampleEvent {
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (player.isCrouching()) {
			if (ConfigHandler.GENERAL._crouchingPreventsTrampling.get()) {
				return;
			}
		}
		
		BlockPos playerpos = player.blockPosition();
		Block block = world.getBlockState(playerpos).getBlock();

		Util.trampleCheck(world, playerpos, block);
	}
}
