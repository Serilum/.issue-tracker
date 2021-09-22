/*
 * This is the latest source code of Healing Campfire.
 * Minecraft version: 1.17.x, mod version: 3.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Healing Campfire ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.healingcampfire.events;

import java.util.List;

import com.natamus.collective_fabric.functions.FABFunctions;
import com.natamus.healingcampfire.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class CampfireEvent {
	public static void playerTickEvent(ServerLevel world, ServerPlayer player) {
		if (player.tickCount % ConfigHandler.checkForCampfireDelayInTicks.getValue() != 0) {
			return;
		}
		
		List<BlockPos> nearbycampfires = FABFunctions.getAllTileEntityPositionsNearbyEntity(BlockEntityType.CAMPFIRE, ConfigHandler.healingRadius.getValue(), world, player);
		if (nearbycampfires.size() == 0) {
			return;
		}
		
		MobEffectInstance effectinstance;
		if (ConfigHandler.hideEffectParticles.getValue()) {
			effectinstance = new MobEffectInstance(MobEffects.REGENERATION, ConfigHandler.effectDurationSeconds.getValue()*20, ConfigHandler.regenerationLevel.getValue()-1, true, false);
		}
		else {
			effectinstance = new MobEffectInstance(MobEffects.REGENERATION, ConfigHandler.effectDurationSeconds.getValue()*20, ConfigHandler.regenerationLevel.getValue()-1);
		}
		
		BlockPos campfire = null;
		for (BlockPos nearbycampfire : nearbycampfires) {
			BlockState campfirestate = world.getBlockState(nearbycampfire);
			Block block = campfirestate.getBlock();
			
			if (!ConfigHandler.enableEffectForNormalCampfires.getValue()) {
				if (block.equals(Blocks.CAMPFIRE)) {
					continue;
				}
			}
			if (!ConfigHandler.enableEffectForSoulCampfires.getValue()) {
				if (block.equals(Blocks.SOUL_CAMPFIRE)) {
					continue;
				}
			}
			
			if (ConfigHandler.campfireMustBeLit.getValue()) {
				Boolean islit = campfirestate.getValue(CampfireBlock.LIT);
				if (!islit) {
					continue;
				}
			}
			if (ConfigHandler.campfireMustBeSignalling.getValue()) {
				Boolean issignalling = campfirestate.getValue(CampfireBlock.SIGNAL_FIRE);
				if (!issignalling) {
					continue;
				}
			}
			
			campfire = nearbycampfire.immutable();
			break;
		}
		
		if (campfire == null) {
			return;
		}
		
		BlockPos ppos = player.blockPosition();
		double r = (double)ConfigHandler.healingRadius.getValue();
		if (ppos.closerThan(campfire, r)) {
			boolean addeffect = true;
			MobEffectInstance currentregen = player.getEffect(effectinstance.getEffect());
			if (currentregen != null) {
				int currentduration = currentregen.getDuration();
				if (currentduration > (ConfigHandler.effectDurationSeconds.getValue()*10)) {
					addeffect = false;
				}
			}
			
			if (addeffect) {
				player.addEffect(effectinstance);
			}
		}
		if (ConfigHandler.healPassiveMobs.getValue()) {
			for (Entity entity : world.getEntities(player, new AABB(campfire.getX()-r, campfire.getY()-r, campfire.getZ()-r, campfire.getX()+r, campfire.getY()+r, campfire.getZ()+r))) {
				if (entity instanceof LivingEntity && (entity instanceof Player == false) && !entity.getType().getCategory().equals(MobCategory.MONSTER)) {
					LivingEntity le = (LivingEntity)entity;
					
					boolean addeffect = true;
					MobEffectInstance currentregen = le.getEffect(effectinstance.getEffect());
					if (currentregen != null) {
						int currentduration = currentregen.getDuration();
						if (currentduration > (ConfigHandler.effectDurationSeconds.getValue()*10)) {
							addeffect = false;
						}
					}
					
					if (addeffect) {
						le.addEffect(effectinstance);
					}
				}
			}
		}
	}
}