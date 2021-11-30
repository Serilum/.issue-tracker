/*
 * This is the latest source code of Campfire Spawn and Tweaks.
 * Minecraft version: 1.18.x, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Campfire Spawn and Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.campfirespawnandtweaks.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.campfirespawnandtweaks.config.ConfigHandler;
import com.natamus.campfirespawnandtweaks.util.Util;
import com.natamus.collective_fabric.functions.BlockFunctions;
import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.collective_fabric.functions.WorldFunctions;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class CampfireEvent {
	public static HashMap<String, Pair<Level, BlockPos>> playercampfires = new HashMap<String, Pair<Level, BlockPos>>();
	
	public static HashMap<Level, List<Pair<Player, BlockPos>>> playerstorespawn = new HashMap<Level, List<Pair<Player, BlockPos>>>();
	private static HashMap<Level, List<BlockPos>> firestoextinguish = new HashMap<Level, List<BlockPos>>();
	
	private static List<Block> extinguishblocks = new ArrayList<Block>(Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.SAND, Blocks.RED_SAND, Blocks.SOUL_SAND));
	
	public static void onWorldLoad(ServerLevel world) {
		firestoextinguish.put(world, new ArrayList<BlockPos>());
		playerstorespawn.put(world, new ArrayList<Pair<Player, BlockPos>>());
		Util.loadCampfireSpawnsFromWorld(world);
	}
	
	public static void onWorldTick(ServerLevel world) {
		if (firestoextinguish.get(world).size() > 0) {
			BlockPos campfirepos = firestoextinguish.get(world).get(0);
			BlockState state = world.getBlockState(campfirepos);
			if (state.getBlock() instanceof CampfireBlock) {
				world.setBlockAndUpdate(campfirepos, state.setValue(CampfireBlock.LIT, false).setValue(CampfireBlock.WATERLOGGED, false));
			}
			
			firestoextinguish.get(world).remove(0);
		}
		if (playerstorespawn.get(world).size() > 0) {
			Pair<Player, BlockPos> pair = playerstorespawn.get(world).get(0);
			Player player = pair.getFirst();
			BlockPos respawnpos = pair.getSecond();
			
			if (player instanceof ServerPlayer) {
				if (world.getBlockState(respawnpos).getBlock() instanceof CampfireBlock) {
					ServerPlayer serverplayer = ((ServerPlayer)player);
					ServerLevel serverworld = (ServerLevel)world;
					
					Vec3 ts;
					
					int fireresistancems = ConfigHandler.fireResitanceDurationOnRespawnInMs.getValue();
					if (fireresistancems > 0) {
						ts = new Vec3(respawnpos.getX()+0.5, respawnpos.getY()+0.5, respawnpos.getZ()+0.5);
						EntityFunctions.addPotionEffect(player, MobEffects.FIRE_RESISTANCE, fireresistancems);
						
					}
					else {
						ts = new Vec3(respawnpos.getX()+1.5, respawnpos.getY(), respawnpos.getZ()+0.5);
					}
					
					if (ConfigHandler.createAirPocketIfBlocksAboveCampfire.getValue()) {
						BlockPos tsbp = new BlockPos(ts.x, ts.y, ts.z);
						Iterator<BlockPos> posaround = BlockPos.betweenClosedStream(tsbp.getX(), tsbp.getY(), tsbp.getZ(), tsbp.getX(), tsbp.getY()+1, tsbp.getZ()).iterator();
						while (posaround.hasNext()) {
							BlockPos around = posaround.next();
							Block block = world.getBlockState(around).getBlock();
							if (block.equals(Blocks.AIR) || block instanceof CampfireBlock) {
								continue;
							}
							
							BlockFunctions.dropBlock(world, around);
						}
						
					}
					
					serverplayer.teleportTo(serverworld, ts.x, ts.y, ts.z, player.getYRot(), player.getXRot());
				}
				else {
					String playername = player.getName().toString();
					playercampfires.remove(playername.toLowerCase());
					if (ConfigHandler.sendMessageOnCampfireSpawnMissing.getValue()) {
						StringFunctions.sendMessage(player, "Campfire spawn point missing.", ChatFormatting.DARK_GRAY);
					}
				}
			}
			
			playerstorespawn.get(world).remove(0);
		}
	}
	
	public static boolean onEntityBlockPlace(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack itemStack) {
		if (world.isClientSide) {
			return true;
		}
		
		if (entity instanceof Player == false) {
			return true;
		}
		
		Block block = state.getBlock();
		if (block instanceof CampfireBlock) {
			Player player = (Player)entity;
			if (player.getMainHandItem().getItem() instanceof FlintAndSteelItem || player.getOffhandItem().getItem() instanceof FlintAndSteelItem) {
				return true;
			}
			
			if (ConfigHandler.campfiresStartUnlit.getValue()) {
				world.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean onRightClickCampfireBlock(Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
		if (world.isClientSide) {
			return true;
		}
		
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		boolean allowAction = true;
		if (block instanceof CampfireBlock) {
			String playername = player.getName().getString();
			if (player.isShiftKeyDown()) {
				if (ConfigHandler.sneakRightClickCampfireToUnset.getValue()) {
					if (Util.checkForCampfireSpawnRemoval(world, playername, pos)) {
						if (ConfigHandler.sendMessageOnNewCampfireSpawnSet.getValue()) {
							StringFunctions.sendMessage(player, "Campfire spawn point removed.", ChatFormatting.DARK_GRAY);
						}
					}
					return true;
				}
			}
			
			ItemStack itemstack = player.getItemInHand(hand);
			Item item = itemstack.getItem();
			
			boolean holdinglighter = false;
			if (player.getMainHandItem().getItem() instanceof FlintAndSteelItem || player.getOffhandItem().getItem() instanceof FlintAndSteelItem) {
				holdinglighter = true;
				if (state.getValue(CampfireBlock.LIT)) {
					allowAction = false;
				}
			}
			
			boolean removed = false;
			if (state.getValue(CampfireBlock.LIT) || holdinglighter) {				
				boolean iswaterbucket = item.equals(Items.WATER_BUCKET);
				Block itemblock = Block.byItem(item);
				if (extinguishblocks.contains(itemblock) || iswaterbucket && !holdinglighter) {
					if (!player.isCreative() && !iswaterbucket) {
						itemstack.shrink(1);
					}
					
					allowAction = false;
					world.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
					
					if (iswaterbucket) {
						firestoextinguish.get(world).add(pos);
					}
					
					if (Util.checkForCampfireSpawnRemoval(world, playername, pos)) {
						if (ConfigHandler.sendMessageOnNewCampfireSpawnSet.getValue()) {
							StringFunctions.sendMessage(player, "Campfire spawn point removed.", ChatFormatting.DARK_GRAY);
						}
					}
					removed = true;
				}
				

				if (!removed && hand.equals(InteractionHand.MAIN_HAND) && (holdinglighter || itemstack.isEmpty())) {
					boolean replaced = playercampfires.containsKey(playername.toLowerCase());
					BlockPos oldpos = null;
					if (replaced) {
						oldpos = playercampfires.get(playername.toLowerCase()).getSecond().immutable();
					}
					
					if (Util.setCampfireSpawn(world, playername, pos)) {
						if (ConfigHandler.sendMessageOnNewCampfireSpawnSet.getValue()) {
							if (holdinglighter) {
								world.setBlockAndUpdate(pos, state.setValue(CampfireBlock.WATERLOGGED, false));
								player.swing(hand);
							}
							
							if (replaced) {
								if (oldpos.equals(pos)) {
									StringFunctions.sendMessage(player, "Campfire spawn point remains the same.", ChatFormatting.DARK_GRAY);
									return true;
								}
								StringFunctions.sendMessage(player, "Campfire spawn point replaced.", ChatFormatting.DARK_GRAY);
								return true;
							}
							
							StringFunctions.sendMessage(player, "Campfire spawn point set.", ChatFormatting.DARK_GRAY);
						}
					}
				}
			}
		}
		else if (block instanceof BedBlock) {
			if (player.isShiftKeyDown()) {
				if (!ConfigHandler.bedsOverrideCampfireSpawnOnSneakRightClick.getValue()) {
					return true;
				}
				
				String playername = player.getName().getString().toLowerCase();
				if (playercampfires.containsKey(playername)) {
					BlockPos newspawn = pos.immutable();

					Pair<Level, BlockPos> pair = playercampfires.get(playername);
					Level oldworld = pair.getFirst();
					BlockPos oldpos = pair.getSecond();
					
					if (WorldFunctions.getWorldDimensionName(world).equals(WorldFunctions.getWorldDimensionName(oldworld))) {
						if (newspawn.equals(oldpos)) {
							return true;
						}
					}
					
					if (Util.checkForCampfireSpawnRemoval(world, playername, oldpos)) {
						if (ConfigHandler.sendMessageOnCampfireSpawnOverride.getValue()) {
							StringFunctions.sendMessage(player, "Campfire spawn point unset.", ChatFormatting.DARK_GRAY);
						}
					}
				}
			}
		}
		
		return allowAction;
	}
	
	public static void onCampfireBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return;
		}
		
		if (world.getBlockState(pos).getBlock() instanceof CampfireBlock) {
			String playername = player.getName().getString().toLowerCase();

			if (Util.checkForCampfireSpawnRemoval(world, playername, pos)) {
				if (ConfigHandler.sendMessageOnNewCampfireSpawnSet.getValue()) {
					StringFunctions.sendMessage(player, "Campfire spawn point removed.", ChatFormatting.DARK_GRAY);
				}
			}
		}
		
	}
	
	public static void onPlayerRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
		Level world = newPlayer.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		String playername = newPlayer.getName().getString().toLowerCase();
		if (!playercampfires.containsKey(playername)) {
			return;
		}
		
		Pair<Level, BlockPos> pair = playercampfires.get(playername);
		playerstorespawn.get(pair.getFirst()).add(new Pair<Player, BlockPos>(newPlayer, pair.getSecond().immutable()));
	}
}