/*
 * This is the latest source code of Bouncier Beds.
 * Minecraft version: 1.18.x, mod version: 1.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Bouncier Beds ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.bouncierbeds.events;

import com.natamus.bouncierbeds.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;

public class EntityEvent {
	public static void onLivingJump(Level world, Entity entity) {
		if (entity instanceof Player == false) {
			return;
		}
		
		Player player = (Player)entity;
		BlockPos ppos = player.blockPosition();
		
		Block blockfeet = world.getBlockState(ppos).getBlock();
		Block blockbelowfeet = world.getBlockState(ppos.below()).getBlock();
		if ((blockfeet instanceof BedBlock == false) && (blockbelowfeet instanceof BedBlock == false)) {
			return;
		}
		
		player.setDeltaMovement(player.getDeltaMovement().add(0.0f, ConfigHandler.bedBounciness.getValue().floatValue(), 0.0f));
		player.hurtMarked = true;
	}
	
	public static int onFall(Level world, Entity entity, float f, float g) {
		if (!ConfigHandler.bedsPreventFallDamage.getValue()) {
			return 1;
		}
		
		if (world.isClientSide) {
			return 1;
		}
		
		if (entity instanceof Player == false) {
			return 1;
		}
		
		BlockPos ppos = entity.blockPosition();
		Block blockfeet = world.getBlockState(ppos).getBlock();
		Block blockbelowfeet = world.getBlockState(ppos.below()).getBlock();
		if (blockfeet instanceof BedBlock || blockbelowfeet instanceof BedBlock) {
			return 0;
		}
		
		return 1;
	}
}
