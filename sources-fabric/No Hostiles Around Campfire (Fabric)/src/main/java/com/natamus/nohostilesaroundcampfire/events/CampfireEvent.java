/*
 * This is the latest source code of No Hostiles Around Campfire.
 * Minecraft version: 1.17.x, mod version: 3.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of No Hostiles Around Campfire ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.nohostilesaroundcampfire.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.collective_fabric.functions.FABFunctions;
import com.natamus.nohostilesaroundcampfire.config.ConfigHandler;
import com.natamus.nohostilesaroundcampfire.util.Reference;
import com.natamus.nohostilesaroundcampfire.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

public class CampfireEvent {
	private static HashMap<Level, List<BlockPos>> checkCampfireBurn = new HashMap<Level, List<BlockPos>>();
	
	public static void onWorldLoad(ServerLevel world) {
		checkCampfireBurn.put(world, new ArrayList<BlockPos>());
	}
	
	public static void onWorldTick(ServerLevel world) {
		if (checkCampfireBurn.get(world).size() > 0) {
			BlockPos campfirepos = checkCampfireBurn.get(world).get(0);
			BlockState campfirestate = world.getBlockState(campfirepos);
			if (campfirestate.getBlock() instanceof CampfireBlock) {
				boolean islit = true;
				if (ConfigHandler.campfireMustBeLit.getValue()) {
					islit = campfirestate.getValue(CampfireBlock.LIT);
				}
				
				if (islit) {
					int r = (int)(ConfigHandler.preventHostilesRadius.getValue() * ConfigHandler.burnHostilesRadiusModifier.getValue());
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
	
	public static boolean onEntityJoin(Mob entity, ServerLevel world, BlockPos spawnerPos, MobSpawnType spawnReason) {
		if (entity.getTags().contains(Reference.MOD_ID + ".checked" )) {
			return true;	
		}
		entity.addTag(Reference.MOD_ID + ".checked");
		
		if (!ConfigHandler.preventMobSpawnerSpawns.getValue()) {
			if (EntityFunctions.isEntityFromSpawner(entity)) {
				return true;
			}
		}
		
		if (!Util.entityIsHostile(entity)) {
			return true;
		}
		
		List<BlockPos> nearbycampfires = FABFunctions.getAllTileEntityPositionsNearbyEntity(BlockEntityType.CAMPFIRE, ConfigHandler.preventHostilesRadius.getValue(), world, entity);
		if (nearbycampfires.size() == 0) {
			return true;
		}
		
		BlockPos campfire = null;
		for (BlockPos nearbycampfire : nearbycampfires) {
			BlockState campfirestate = world.getBlockState(nearbycampfire);
			Block block = campfirestate.getBlock();
			if (block instanceof CampfireBlock == false) {
				continue;
			}
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
			return true;
		}
		
		List<Entity> passengers = entity.getPassengers();
		if (passengers.size() > 0) {
			for (Entity passenger : passengers) {
				passenger.remove(RemovalReason.DISCARDED);
			}
		}
		
		return false;
	}
	
	public static void onCampfirePlace(Level world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
		if (world.isClientSide) {
			return;
		}
		
		if (!ConfigHandler.burnHostilesAroundWhenPlaced.getValue()) {
			return;
		}
		
		Block block = blockState.getBlock();
		if (block instanceof CampfireBlock == false) {
			return;
		}
		
		if (!ConfigHandler.enableEffectForNormalCampfires.getValue()) {
			if (block.equals(Blocks.CAMPFIRE)) {
				return;
			}
		}
		if (!ConfigHandler.enableEffectForSoulCampfires.getValue()) {
			if (block.equals(Blocks.SOUL_CAMPFIRE)) {
				return;
			}
		}
		
		checkCampfireBurn.get(world).add(blockPos.immutable());
	}
	
	public static InteractionResult onRightClickCampfireBlock(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		BlockPos pos = BlockPosFunctions.getBlockPosFromHitResult(hitResult);
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if (block instanceof CampfireBlock) {
			if (state.getValue(CampfireBlock.LIT)) {
				return InteractionResult.PASS;
			}
			
			if (player.getMainHandItem().getItem() instanceof FlintAndSteelItem || player.getOffhandItem().getItem() instanceof FlintAndSteelItem) {
				checkCampfireBurn.get(world).add(pos.immutable());
			}
		}
		
		return InteractionResult.PASS;
	}
	
	public static void onCampfireBreak(Level world, Player player, BlockPos ppos, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack) {
		if (world.isClientSide) {
			return;
		}
		
		if (!ConfigHandler.burnHostilesAroundWhenPlaced.getValue()) {
			return;
		}
		
		Block block = blockState.getBlock();
		if (block instanceof CampfireBlock == false) {
			return;
		}
		
		if (!ConfigHandler.enableEffectForNormalCampfires.getValue()) {
			if (block.equals(Blocks.CAMPFIRE)) {
				return;
			}
		}
		if (!ConfigHandler.enableEffectForSoulCampfires.getValue()) {
			if (block.equals(Blocks.SOUL_CAMPFIRE)) {
				return;
			}
		}

		int r = (int)(ConfigHandler.preventHostilesRadius.getValue() * ConfigHandler.burnHostilesRadiusModifier.getValue());
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