/*
 * This is the latest source code of Trample Everything.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Trample Everything ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.trampleeverything.events;

import com.natamus.trampleeverything.config.ConfigHandler;
import com.natamus.trampleeverything.util.Util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class TrampleEvent {
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		PlayerEntity player = e.player;
		World world = player.getCommandSenderWorld();
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
