/*
 * This is the latest source code of No Hostiles Around Campfire.
 * Minecraft version: 1.19.2, mod version: 4.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.nohostilesaroundcampfire.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.natamus.collective.functions.FABFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.nohostilesaroundcampfire.config.ConfigHandler;
import com.natamus.nohostilesaroundcampfire.util.Reference;
import com.natamus.nohostilesaroundcampfire.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CampfireEvent {
	private static HashMap<Level, List<BlockPos>> checkCampfireBurn = new HashMap<Level, List<BlockPos>>();
	
	@SubscribeEvent
	public void onWorldLoad(LevelEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		checkCampfireBurn.put(world, new ArrayList<BlockPos>());
	}
	
	@SubscribeEvent
	public void onWorldTick(LevelTickEvent e) {
		Level world = e.level;
		if (world.isClientSide || !e.phase.equals(Phase.END)) {
			return;
		}
		
		if (checkCampfireBurn.get(world).size() > 0) {
			BlockPos campfirepos = checkCampfireBurn.get(world).get(0);
			BlockState campfirestate = world.getBlockState(campfirepos);
			if (campfirestate.getBlock() instanceof CampfireBlock) {
				boolean islit = true;
				if (ConfigHandler.GENERAL.campfireMustBeLit.get()) {
					islit = campfirestate.getValue(CampfireBlock.LIT);
				}
				
				if (islit) {
					int r = (int)(ConfigHandler.GENERAL.preventHostilesRadius.get() * ConfigHandler.GENERAL.burnHostilesRadiusModifier.get());
					List<Entity> entities = world.getEntities(null, new AABB(campfirepos.getX()-r, campfirepos.getY()-r, campfirepos.getZ()-r, campfirepos.getX()+r, campfirepos.getY()+r, campfirepos.getZ()+r));
					for (Entity entity : entities) {
						if (Util.entityIsHostile(entity)) {
							entity.setSecondsOnFire(30);
						}
					}
				}
			}
			
			checkCampfireBurn.get(world).remove(0);
		}
	}
	
	@SubscribeEvent
	public void onEntityJoin(LivingSpawnEvent.CheckSpawn e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		
		if (entity.getTags().contains(Reference.MOD_ID + ".checked" )) {
			return;	
		}
		entity.addTag(Reference.MOD_ID + ".checked");
		
		if (!ConfigHandler.GENERAL.preventMobSpawnerSpawns.get()) {
			BaseSpawner msbl = e.getSpawner();
			if (msbl != null) {
				return;
			}
		}
		
		if (!Util.entityIsHostile(entity)) {
			return;
		}
		
		List<BlockPos> nearbycampfires = FABFunctions.getAllTileEntityPositionsNearbyEntity(BlockEntityType.CAMPFIRE, ConfigHandler.GENERAL.preventHostilesRadius.get(), world, entity);
		if (nearbycampfires.size() == 0) {
			return;
		}
		
		BlockPos campfire = null;
		for (BlockPos nearbycampfire : nearbycampfires) {
			BlockState campfirestate = world.getBlockState(nearbycampfire);
			Block block = campfirestate.getBlock();
			if (block instanceof CampfireBlock == false) {
				continue;
			}
			if (!ConfigHandler.GENERAL.enableEffectForNormalCampfires.get()) {
				if (block.equals(Blocks.CAMPFIRE)) {
					continue;
				}
			}
			if (!ConfigHandler.GENERAL.enableEffectForSoulCampfires.get()) {
				if (block.equals(Blocks.SOUL_CAMPFIRE)) {
					continue;
				}
			}
			
			if (ConfigHandler.GENERAL.campfireMustBeLit.get()) {
				Boolean islit = campfirestate.getValue(CampfireBlock.LIT);
				if (!islit) {
					continue;
				}
			}
			if (ConfigHandler.GENERAL.campfireMustBeSignalling.get()) {
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
		
		List<Entity> passengers = entity.getPassengers();
		if (passengers.size() > 0) {
			for (Entity passenger : passengers) {
				passenger.remove(RemovalReason.DISCARDED);
			}
		}
		
		e.setResult(Result.DENY);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onCampfirePlace(BlockEvent.EntityPlaceEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		if (!ConfigHandler.GENERAL.burnHostilesAroundWhenPlaced.get()) {
			return;
		}
		
		BlockState state = e.getPlacedBlock();
		Block block = state.getBlock();
		if (block instanceof CampfireBlock == false) {
			return;
		}
		
		if (e.isCanceled()) {
			return;
		}
		
		if (!ConfigHandler.GENERAL.enableEffectForNormalCampfires.get()) {
			if (block.equals(Blocks.CAMPFIRE)) {
				return;
			}
		}
		if (!ConfigHandler.GENERAL.enableEffectForSoulCampfires.get()) {
			if (block.equals(Blocks.SOUL_CAMPFIRE)) {
				return;
			}
		}
		
		BlockPos pos = e.getPos();
		checkCampfireBurn.get(world).add(pos.immutable());
	}
	
	@SubscribeEvent
	public void onRightClickCampfireBlock(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Player player = e.getEntity();
		BlockPos pos = e.getPos();
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if (block instanceof CampfireBlock) {
			if (state.getValue(CampfireBlock.LIT)) {
				return;
			}
			
			if (player.getMainHandItem().getItem() instanceof FlintAndSteelItem || player.getOffhandItem().getItem() instanceof FlintAndSteelItem) {
				checkCampfireBurn.get(world).add(pos.immutable());
			}
		}
	}
	
	@SubscribeEvent
	public void onCampfireBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		if (!ConfigHandler.GENERAL.burnHostilesAroundWhenPlaced.get()) {
			return;
		}
		
		Block block = e.getState().getBlock();
		if (block instanceof CampfireBlock == false) {
			return;
		}
		
		if (!ConfigHandler.GENERAL.enableEffectForNormalCampfires.get()) {
			if (block.equals(Blocks.CAMPFIRE)) {
				return;
			}
		}
		if (!ConfigHandler.GENERAL.enableEffectForSoulCampfires.get()) {
			if (block.equals(Blocks.SOUL_CAMPFIRE)) {
				return;
			}
		}

		int r = (int)(ConfigHandler.GENERAL.preventHostilesRadius.get() * ConfigHandler.GENERAL.burnHostilesRadiusModifier.get());
		BlockPos ppos = e.getPos();
		List<Entity> entities = world.getEntities(null, new AABB(ppos.getX()-r, ppos.getY()-r, ppos.getZ()-r, ppos.getX()+r, ppos.getY()+r, ppos.getZ()+r));
		for (Entity entity : entities) {
			if (Util.entityIsHostile(entity)) {
				if (entity.isOnFire()) {
					entity.clearFire();
					entity.setSecondsOnFire(2);
				}
			}
		}	
	}
}