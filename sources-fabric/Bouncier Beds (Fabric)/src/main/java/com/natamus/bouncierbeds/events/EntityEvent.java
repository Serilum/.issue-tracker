/*
 * This is the latest source code of Bouncier Beds.
 * Minecraft version: 1.19.2, mod version: 1.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
