/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.bouncierbeds.events;

import com.natamus.thevanillaexperience.mods.bouncierbeds.config.BouncierBedsConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BouncierBedsEntityEvent {
	@SubscribeEvent
	public void onLivingJump(LivingJumpEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof Player == false) {
			return;
		}
		
		Level world = entity.getCommandSenderWorld();
		Player player = (Player)entity;
		BlockPos ppos = player.blockPosition();
		
		Block blockfeet = world.getBlockState(ppos).getBlock();
		Block blockbelowfeet = world.getBlockState(ppos.below()).getBlock();
		if ((blockfeet instanceof BedBlock == false) && (blockbelowfeet instanceof BedBlock == false)) {
			return;
		}
		
		player.setDeltaMovement(player.getDeltaMovement().add(0.0f, BouncierBedsConfigHandler.GENERAL.bedBounciness.get().floatValue(), 0.0f));
		player.hurtMarked = true;
	}
	
	@SubscribeEvent
	public void onFall(LivingFallEvent e) {
		if (!BouncierBedsConfigHandler.GENERAL.bedsPreventFallDamage.get()) {
			return;
		}
		
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof Player == false) {
			return;
		}
		
		BlockPos ppos = entity.blockPosition();
		Block blockfeet = world.getBlockState(ppos).getBlock();
		Block blockbelowfeet = world.getBlockState(ppos.below()).getBlock();
		if (blockfeet instanceof BedBlock || blockbelowfeet instanceof BedBlock) {
			e.setCanceled(true);
		}
	}
}
