/*
 * This is the latest source code of Campfire Spawn and Tweaks.
 * Minecraft version: 1.18.1, mod version: 1.4.
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
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CampfireEvent {
	public static HashMap<String, Pair<Level, BlockPos>> playercampfires = new HashMap<String, Pair<Level, BlockPos>>();
	
	public static HashMap<Level, List<Pair<Player, BlockPos>>> playerstorespawn = new HashMap<Level, List<Pair<Player, BlockPos>>>();
	private static HashMap<Level, List<BlockPos>> firestoextinguish = new HashMap<Level, List<BlockPos>>();
	
	private static List<Block> extinguishblocks = new ArrayList<Block>(Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.SAND, Blocks.RED_SAND, Blocks.SOUL_SAND));
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		firestoextinguish.put(world, new ArrayList<BlockPos>());
		playerstorespawn.put(world, new ArrayList<Pair<Player, BlockPos>>());
		Util.loadCampfireSpawnsFromWorld(world);
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level world = e.world;
		if (world.isClientSide) {
			return;
		}
		
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
					
					int fireresistancems = ConfigHandler.GENERAL.fireResitanceDurationOnRespawnInMs.get();
					if (fireresistancems > 0) {
						ts = new Vec3(respawnpos.getX()+0.5, respawnpos.getY()+0.5, respawnpos.getZ()+0.5);
						EntityFunctions.addPotionEffect(player, MobEffects.FIRE_RESISTANCE, fireresistancems);
						
					}
					else {
						ts = new Vec3(respawnpos.getX()+1.5, respawnpos.getY(), respawnpos.getZ()+0.5);
					}
					
					if (ConfigHandler.GENERAL.createAirPocketIfBlocksAboveCampfire.get()) {
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
					if (ConfigHandler.GENERAL.sendMessageOnCampfireSpawnMissing.get()) {
						StringFunctions.sendMessage(player, "Campfire spawn point missing.", ChatFormatting.DARK_GRAY);
					}
				}
			}
			
			playerstorespawn.get(world).remove(0);
		}
	}
	
	@SubscribeEvent
	public void onEntityBlockPlace(EntityPlaceEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Player == false) {
			return;
		}
		
		BlockPos pos = e.getPos();
		BlockState state = e.getPlacedBlock();
		Block block = state.getBlock();
		if (block instanceof CampfireBlock) {
			Player player = (Player)entity;
			if (player.getMainHandItem().getItem() instanceof FlintAndSteelItem || player.getOffhandItem().getItem() instanceof FlintAndSteelItem) {
				return;
			}
			
			if (ConfigHandler.GENERAL.campfiresStartUnlit.get()) {
				world.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
			}
		}
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
			String playername = player.getName().getString();
			if (player.isShiftKeyDown()) {
				if (ConfigHandler.GENERAL.sneakRightClickCampfireToUnset.get()) {
					if (Util.checkForCampfireSpawnRemoval(world, playername, pos)) {
						if (ConfigHandler.GENERAL.sendMessageOnNewCampfireSpawnSet.get()) {
							StringFunctions.sendMessage(player, "Campfire spawn point removed.", ChatFormatting.DARK_GRAY);
						}
					}
					return;
				}
			}
			
			ItemStack itemstack = e.getItemStack();
			Item item = itemstack.getItem();
			
			boolean holdinglighter = false;
			if (player.getMainHandItem().getItem() instanceof FlintAndSteelItem || player.getOffhandItem().getItem() instanceof FlintAndSteelItem) {
				holdinglighter = true;
				if (state.getValue(CampfireBlock.LIT)) {
					e.setCanceled(true);
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
					
					e.setCanceled(true);
					world.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
					
					if (iswaterbucket) {
						firestoextinguish.get(world).add(pos);
					}
					
					if (Util.checkForCampfireSpawnRemoval(world, playername, pos)) {
						if (ConfigHandler.GENERAL.sendMessageOnNewCampfireSpawnSet.get()) {
							StringFunctions.sendMessage(player, "Campfire spawn point removed.", ChatFormatting.DARK_GRAY);
						}
					}
					removed = true;
				}
				

				if (!removed && e.getHand().equals(InteractionHand.MAIN_HAND) && (holdinglighter || itemstack.isEmpty())) {
					boolean replaced = playercampfires.containsKey(playername.toLowerCase());
					BlockPos oldpos = null;
					if (replaced) {
						oldpos = playercampfires.get(playername.toLowerCase()).getSecond().immutable();
					}
					
					if (Util.setCampfireSpawn(world, playername, pos)) {
						if (ConfigHandler.GENERAL.sendMessageOnNewCampfireSpawnSet.get()) {
							if (holdinglighter) {
								world.setBlockAndUpdate(pos, state.setValue(CampfireBlock.WATERLOGGED, false));
								player.swing(e.getHand());
							}
							
							if (replaced) {
								if (oldpos.equals(pos)) {
									StringFunctions.sendMessage(player, "Campfire spawn point remains the same.", ChatFormatting.DARK_GRAY);
									return;
								}
								StringFunctions.sendMessage(player, "Campfire spawn point replaced.", ChatFormatting.DARK_GRAY);
								return;
							}
							
							StringFunctions.sendMessage(player, "Campfire spawn point set.", ChatFormatting.DARK_GRAY);
						}
					}
				}
			}
		}
		else if (block instanceof BedBlock) {
			if (player.isShiftKeyDown()) {
				if (!ConfigHandler.GENERAL.bedsOverrideCampfireSpawnOnSneakRightClick.get()) {
					return;
				}
				
				String playername = player.getName().getString().toLowerCase();
				if (playercampfires.containsKey(playername)) {
					BlockPos newspawn = e.getPos();

					Pair<Level, BlockPos> pair = playercampfires.get(playername);
					Level oldworld = pair.getFirst();
					BlockPos oldpos = pair.getSecond();
					
					if (WorldFunctions.getWorldDimensionName(world).equals(WorldFunctions.getWorldDimensionName(oldworld))) {
						if (newspawn.equals(oldpos)) {
							return;
						}
					}
					
					if (Util.checkForCampfireSpawnRemoval(world, playername, oldpos)) {
						if (ConfigHandler.GENERAL.sendMessageOnCampfireSpawnOverride.get()) {
							StringFunctions.sendMessage(player, "Campfire spawn point unset.", ChatFormatting.DARK_GRAY);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onCampfireBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockPos pos = e.getPos();
		if (world.getBlockState(pos).getBlock() instanceof CampfireBlock) {
			Player player = e.getPlayer();
			String playername = player.getName().getString().toLowerCase();

			if (Util.checkForCampfireSpawnRemoval(world, playername, pos)) {
				if (ConfigHandler.GENERAL.sendMessageOnNewCampfireSpawnSet.get()) {
					StringFunctions.sendMessage(player, "Campfire spawn point removed.", ChatFormatting.DARK_GRAY);
				}
			}
		}
		
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		String playername = player.getName().getString().toLowerCase();
		if (!playercampfires.containsKey(playername)) {
			return;
		}
		
		Pair<Level, BlockPos> pair = playercampfires.get(playername);
		playerstorespawn.get(pair.getFirst()).add(new Pair<Player, BlockPos>(player, pair.getSecond().immutable()));
	}
}