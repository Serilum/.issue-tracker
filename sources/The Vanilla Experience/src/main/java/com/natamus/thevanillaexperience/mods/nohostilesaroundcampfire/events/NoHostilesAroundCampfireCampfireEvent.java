/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.nohostilesaroundcampfire.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.natamus.collective.functions.FABFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.nohostilesaroundcampfire.config.NoHostilesAroundCampfireConfigHandler;
import com.natamus.thevanillaexperience.mods.nohostilesaroundcampfire.util.Reference;
import com.natamus.thevanillaexperience.mods.nohostilesaroundcampfire.util.NoHostilesAroundCampfireUtil;

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
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class NoHostilesAroundCampfireCampfireEvent {
	private static HashMap<Level, List<BlockPos>> checkCampfireBurn = new HashMap<Level, List<BlockPos>>();
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		checkCampfireBurn.put(world, new ArrayList<BlockPos>());
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.END)) {
			return;
		}
		
		if (checkCampfireBurn.get(world).size() > 0) {
			BlockPos campfirepos = checkCampfireBurn.get(world).get(0);
			BlockState campfirestate = world.getBlockState(campfirepos);
			if (campfirestate.getBlock() instanceof CampfireBlock) {
				boolean islit = true;
				if (NoHostilesAroundCampfireConfigHandler.GENERAL.campfireMustBeLit.get()) {
					islit = campfirestate.getValue(CampfireBlock.LIT);
				}
				
				if (islit) {
					int r = (int)(NoHostilesAroundCampfireConfigHandler.GENERAL.preventHostilesRadius.get() * NoHostilesAroundCampfireConfigHandler.GENERAL.burnHostilesRadiusModifier.get());
					List<Entity> entities = world.getEntities(null, new AABB(campfirepos.getX()-r, campfirepos.getY()-r, campfirepos.getZ()-r, campfirepos.getX()+r, campfirepos.getY()+r, campfirepos.getZ()+r));
					for (Entity entity : entities) {
						if (NoHostilesAroundCampfireUtil.entityIsHostile(entity)) {
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
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		
		if (entity.getTags().contains(Reference.MOD_ID + ".checked" )) {
			return;	
		}
		entity.addTag(Reference.MOD_ID + ".checked");
		
		if (!NoHostilesAroundCampfireConfigHandler.GENERAL.preventMobSpawnerSpawns.get()) {
			BaseSpawner msbl = e.getSpawner();
			if (msbl != null) {
				return;
			}
		}
		
		if (!NoHostilesAroundCampfireUtil.entityIsHostile(entity)) {
			return;
		}
		
		List<BlockPos> nearbycampfires = FABFunctions.getAllTileEntityPositionsNearbyEntity(BlockEntityType.CAMPFIRE, NoHostilesAroundCampfireConfigHandler.GENERAL.preventHostilesRadius.get(), world, entity);
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
			if (!NoHostilesAroundCampfireConfigHandler.GENERAL.enableEffectForNormalCampfires.get()) {
				if (block.equals(Blocks.CAMPFIRE)) {
					continue;
				}
			}
			if (!NoHostilesAroundCampfireConfigHandler.GENERAL.enableEffectForSoulCampfires.get()) {
				if (block.equals(Blocks.SOUL_CAMPFIRE)) {
					continue;
				}
			}
			
			if (NoHostilesAroundCampfireConfigHandler.GENERAL.campfireMustBeLit.get()) {
				Boolean islit = campfirestate.getValue(CampfireBlock.LIT);
				if (!islit) {
					continue;
				}
			}
			if (NoHostilesAroundCampfireConfigHandler.GENERAL.campfireMustBeSignalling.get()) {
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
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (!NoHostilesAroundCampfireConfigHandler.GENERAL.burnHostilesAroundWhenPlaced.get()) {
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
		
		if (!NoHostilesAroundCampfireConfigHandler.GENERAL.enableEffectForNormalCampfires.get()) {
			if (block.equals(Blocks.CAMPFIRE)) {
				return;
			}
		}
		if (!NoHostilesAroundCampfireConfigHandler.GENERAL.enableEffectForSoulCampfires.get()) {
			if (block.equals(Blocks.SOUL_CAMPFIRE)) {
				return;
			}
		}
		
		BlockPos pos = e.getPos();
		checkCampfireBurn.get(world).add(pos.immutable());
	}
	
	@SubscribeEvent
	public void onRightClickCampfireBlock(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Player player = e.getPlayer();
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
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (!NoHostilesAroundCampfireConfigHandler.GENERAL.burnHostilesAroundWhenPlaced.get()) {
			return;
		}
		
		Block block = e.getState().getBlock();
		if (block instanceof CampfireBlock == false) {
			return;
		}
		
		if (!NoHostilesAroundCampfireConfigHandler.GENERAL.enableEffectForNormalCampfires.get()) {
			if (block.equals(Blocks.CAMPFIRE)) {
				return;
			}
		}
		if (!NoHostilesAroundCampfireConfigHandler.GENERAL.enableEffectForSoulCampfires.get()) {
			if (block.equals(Blocks.SOUL_CAMPFIRE)) {
				return;
			}
		}

		int r = (int)(NoHostilesAroundCampfireConfigHandler.GENERAL.preventHostilesRadius.get() * NoHostilesAroundCampfireConfigHandler.GENERAL.burnHostilesRadiusModifier.get());
		BlockPos ppos = e.getPos();
		List<Entity> entities = world.getEntities(null, new AABB(ppos.getX()-r, ppos.getY()-r, ppos.getZ()-r, ppos.getX()+r, ppos.getY()+r, ppos.getZ()+r));
		for (Entity entity : entities) {
			if (NoHostilesAroundCampfireUtil.entityIsHostile(entity)) {
				if (entity.isOnFire()) {
					entity.clearFire();
					entity.setSecondsOnFire(2);
				}
			}
		}	
	}
}