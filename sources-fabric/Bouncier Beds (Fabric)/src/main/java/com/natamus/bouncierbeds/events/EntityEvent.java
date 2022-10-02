/*
 * This is the latest source code of Bouncier Beds.
 * Minecraft version: 1.19.2, mod version: 1.6.
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
		
		player.setDeltaMovement(player.getDeltaMovement().add(0.0f, (float)ConfigHandler.bedBounciness, 0.0f));
		player.hurtMarked = true;
	}
	
	public static int onFall(Level world, Entity entity, float f, float g) {
		if (!ConfigHandler.bedsPreventFallDamage) {
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
