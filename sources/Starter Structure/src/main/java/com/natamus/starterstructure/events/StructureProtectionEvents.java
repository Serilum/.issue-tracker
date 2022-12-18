/*
 * This is the latest source code of Starter Structure.
 * Minecraft version: 1.19.3, mod version: 1.2.
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

package com.natamus.starterstructure.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.starterstructure.config.ConfigHandler;
import com.natamus.starterstructure.util.Reference;
import com.natamus.starterstructure.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class StructureProtectionEvents {
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		if (ConfigHandler.GENERAL.playersInCreativeModeIgnoreProtection.get()) {
			Player player = e.getPlayer();
			if (player.isCreative()) {
				return;
			}
		}

		if (Util.protectedMap.containsKey(level)) {
			if (Util.protectedMap.get(level).contains(e.getPos())) {
				e.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onBlockPlace(BlockEvent.EntityPlaceEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		if (ConfigHandler.GENERAL.playersInCreativeModeIgnoreProtection.get()) {
			Entity entity = e.getEntity();
			if (entity instanceof Player) {
				if (((Player) entity).isCreative()) {
					return;
				}
			}
		}

		if (Util.protectedMap.containsKey(level)) {
			if (Util.protectedMap.get(level).contains(e.getPos())) {
				e.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onPistonMove(PistonEvent.Pre e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		BlockPos pos = e.getFaceOffsetPos();
		BlockPos nextPos = pos.relative(e.getDirection());

		if (Util.protectedMap.containsKey(level)) {
			if (Util.protectedMap.get(level).contains(pos) || Util.protectedMap.get(level).contains(nextPos)) {
				e.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onTNTExplode(ExplosionEvent.Detonate e) {
		Level level = e.getLevel();
		if (level.isClientSide) {
			return;
		}

		boolean cancel = false;
		if (Util.protectedMap.containsKey(level)) {
			for (BlockPos affectedPos : e.getAffectedBlocks()) {
				if (Util.protectedMap.get(level).contains(affectedPos)) {
					cancel = true;
					break;
				}
			}
		}

		if (cancel) {
			e.getAffectedBlocks().clear();
			e.getAffectedEntities().clear();
		}
	}

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent e) {
		if (ConfigHandler.GENERAL.protectSpawnedEntities.get()) {
			if (e.getEntity().getTags().contains(Reference.MOD_ID + ".protected")) {
				if (ConfigHandler.GENERAL.playersInCreativeModeIgnoreEntityProtection.get()) {
					DamageSource damageSource = e.getSource();
					if (damageSource.getEntity() instanceof Player) {
						if (((Player)damageSource.getEntity()).isCreative()) {
							return;
						}
					}
				}
				e.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoin(EntityJoinLevelEvent e) {
		Level level = e.getLevel();
		if (level.isClientSide) {
			return;
		}

		Entity entity = e.getEntity();
		if (entity.getTags().contains(Reference.MOD_ID + ".protected")) {
			if (ConfigHandler.GENERAL.preventSpawnedEntityMovement.get()) {
				if (entity instanceof LivingEntity) {
					((LivingEntity)entity).getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
				}
			}
		}
	}
}
