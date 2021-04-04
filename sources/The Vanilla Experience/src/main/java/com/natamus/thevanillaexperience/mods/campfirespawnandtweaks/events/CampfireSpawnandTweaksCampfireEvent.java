/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.campfirespawnandtweaks.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.thevanillaexperience.mods.campfirespawnandtweaks.config.CampfireSpawnandTweaksConfigHandler;
import com.natamus.thevanillaexperience.mods.campfirespawnandtweaks.util.CampfireSpawnandTweaksUtil;
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CampfireSpawnandTweaksCampfireEvent {
	public static HashMap<String, Pair<World, BlockPos>> playercampfires = new HashMap<String, Pair<World, BlockPos>>();
	
	public static HashMap<World, List<Pair<PlayerEntity, BlockPos>>> playerstorespawn = new HashMap<World, List<Pair<PlayerEntity, BlockPos>>>();
	private static HashMap<World, List<BlockPos>> firestoextinguish = new HashMap<World, List<BlockPos>>();
	
	private static List<Block> extinguishblocks = new ArrayList<Block>(Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.SAND, Blocks.RED_SAND, Blocks.SOUL_SAND));
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		firestoextinguish.put(world, new ArrayList<BlockPos>());
		playerstorespawn.put(world, new ArrayList<Pair<PlayerEntity, BlockPos>>());
		CampfireSpawnandTweaksUtil.loadCampfireSpawnsFromWorld(world);
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		World world = e.world;
		if (world.isRemote) {
			return;
		}
		
		if (firestoextinguish.get(world).size() > 0) {
			BlockPos campfirepos = firestoextinguish.get(world).get(0);
			BlockState state = world.getBlockState(campfirepos);
			if (state.getBlock() instanceof CampfireBlock) {
				world.setBlockState(campfirepos, state.with(CampfireBlock.LIT, false).with(CampfireBlock.WATERLOGGED, false));
			}
			
			firestoextinguish.get(world).remove(0);
		}
		if (playerstorespawn.get(world).size() > 0) {
			Pair<PlayerEntity, BlockPos> pair = playerstorespawn.get(world).get(0);
			PlayerEntity player = pair.getFirst();
			BlockPos respawnpos = pair.getSecond();
			
			if (player instanceof ServerPlayerEntity) {
				if (world.getBlockState(respawnpos).getBlock() instanceof CampfireBlock) {
					ServerPlayerEntity serverplayer = ((ServerPlayerEntity)player);
					ServerWorld serverworld = (ServerWorld)world;
					
					Vector3d ts;
					
					int fireresistancems = CampfireSpawnandTweaksConfigHandler.GENERAL.fireResitanceDurationOnRespawnInMs.get();
					if (fireresistancems > 0) {
						ts = new Vector3d(respawnpos.getX()+0.5, respawnpos.getY()+0.5, respawnpos.getZ()+0.5);
						EntityFunctions.addPotionEffect(player, Effects.FIRE_RESISTANCE, fireresistancems);
						
					}
					else {
						ts = new Vector3d(respawnpos.getX()+1.5, respawnpos.getY(), respawnpos.getZ()+0.5);
					}
					
					if (CampfireSpawnandTweaksConfigHandler.GENERAL.createAirPocketIfBlocksAboveCampfire.get()) {
						BlockPos tsbp = new BlockPos(ts.x, ts.y, ts.z);
						Iterator<BlockPos> posaround = BlockPos.getAllInBox(tsbp.getX(), tsbp.getY(), tsbp.getZ(), tsbp.getX(), tsbp.getY()+1, tsbp.getZ()).iterator();
						while (posaround.hasNext()) {
							BlockPos around = posaround.next();
							Block block = world.getBlockState(around).getBlock();
							if (block.equals(Blocks.AIR) || block instanceof CampfireBlock) {
								continue;
							}
							
							BlockFunctions.dropBlock(world, around);
						}
						
					}
					
					serverplayer.teleport(serverworld, ts.x, ts.y, ts.z, player.rotationYaw, player.rotationPitch);
				}
				else {
					String playername = player.getName().toString();
					playercampfires.remove(playername.toLowerCase());
					if (CampfireSpawnandTweaksConfigHandler.GENERAL.sendMessageOnCampfireSpawnMissing.get()) {
						StringFunctions.sendMessage(player, "Campfire spawn point missing.", TextFormatting.DARK_GRAY);
					}
				}
			}
			
			playerstorespawn.get(world).remove(0);
		}
	}
	
	@SubscribeEvent
	public void onEntityBlockPlace(EntityPlaceEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		BlockPos pos = e.getPos();
		BlockState state = e.getPlacedBlock();
		Block block = state.getBlock();
		if (block instanceof CampfireBlock) {
			PlayerEntity player = (PlayerEntity)entity;
			if (player.getHeldItemMainhand().getItem() instanceof FlintAndSteelItem || player.getHeldItemOffhand().getItem() instanceof FlintAndSteelItem) {
				return;
			}
			
			if (CampfireSpawnandTweaksConfigHandler.GENERAL.campfiresStartUnlit.get()) {
				world.setBlockState(pos, state.with(CampfireBlock.LIT, false));
			}
		}
	}
	
	@SubscribeEvent
	public void onRightClickCampfireBlock(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
		BlockPos pos = e.getPos();
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if (block instanceof CampfireBlock) {
			String playername = player.getName().getString();
			if (player.isSneaking()) {
				if (CampfireSpawnandTweaksConfigHandler.GENERAL.sneakRightClickCampfireToUnset.get()) {
					if (CampfireSpawnandTweaksUtil.checkForCampfireSpawnRemoval(world, playername, pos)) {
						if (CampfireSpawnandTweaksConfigHandler.GENERAL.sendMessageOnNewCampfireSpawnSet.get()) {
							StringFunctions.sendMessage(player, "Campfire spawn point removed.", TextFormatting.DARK_GRAY);
						}
					}
					return;
				}
			}
			
			ItemStack itemstack = e.getItemStack();
			Item item = itemstack.getItem();
			
			boolean holdinglighter = false;
			if (player.getHeldItemMainhand().getItem() instanceof FlintAndSteelItem || player.getHeldItemOffhand().getItem() instanceof FlintAndSteelItem) {
				holdinglighter = true;
				if (state.get(CampfireBlock.LIT)) {
					e.setCanceled(true);
				}
			}
			
			boolean removed = false;
			if (state.get(CampfireBlock.LIT) || holdinglighter) {				
				boolean iswaterbucket = item.equals(Items.WATER_BUCKET);
				Block itemblock = Block.getBlockFromItem(item);
				if (extinguishblocks.contains(itemblock) || iswaterbucket && !holdinglighter) {
					if (!player.isCreative() && !iswaterbucket) {
						itemstack.shrink(1);
					}
					
					e.setCanceled(true);
					world.setBlockState(pos, state.with(CampfireBlock.LIT, false));
					
					if (iswaterbucket) {
						firestoextinguish.get(world).add(pos);
					}
					
					if (CampfireSpawnandTweaksUtil.checkForCampfireSpawnRemoval(world, playername, pos)) {
						if (CampfireSpawnandTweaksConfigHandler.GENERAL.sendMessageOnNewCampfireSpawnSet.get()) {
							StringFunctions.sendMessage(player, "Campfire spawn point removed.", TextFormatting.DARK_GRAY);
						}
					}
					removed = true;
				}
				

				if (!removed && e.getHand().equals(Hand.MAIN_HAND) && (holdinglighter || itemstack.isEmpty())) {
					boolean replaced = playercampfires.containsKey(playername.toLowerCase());
					BlockPos oldpos = null;
					if (replaced) {
						oldpos = playercampfires.get(playername.toLowerCase()).getSecond().toImmutable();
					}
					
					if (CampfireSpawnandTweaksUtil.setCampfireSpawn(world, playername, pos)) {
						if (CampfireSpawnandTweaksConfigHandler.GENERAL.sendMessageOnNewCampfireSpawnSet.get()) {
							if (holdinglighter) {
								world.setBlockState(pos, state.with(CampfireBlock.WATERLOGGED, false));
								player.swingArm(e.getHand());
							}
							
							if (replaced) {
								if (oldpos.equals(pos)) {
									StringFunctions.sendMessage(player, "Campfire spawn point remains the same.", TextFormatting.DARK_GRAY);
									return;
								}
								StringFunctions.sendMessage(player, "Campfire spawn point replaced.", TextFormatting.DARK_GRAY);
								return;
							}
							
							StringFunctions.sendMessage(player, "Campfire spawn point set.", TextFormatting.DARK_GRAY);
						}
					}
				}
			}
		}
		else if (block instanceof BedBlock) {
			if (player.isSneaking()) {
				if (!CampfireSpawnandTweaksConfigHandler.GENERAL.bedsOverrideCampfireSpawnOnSneakRightClick.get()) {
					return;
				}
				
				String playername = player.getName().getString().toLowerCase();
				if (playercampfires.containsKey(playername)) {
					BlockPos newspawn = e.getPos();

					Pair<World, BlockPos> pair = playercampfires.get(playername);
					World oldworld = pair.getFirst();
					BlockPos oldpos = pair.getSecond();
					
					if (WorldFunctions.getWorldDimensionName(world).equals(WorldFunctions.getWorldDimensionName(oldworld))) {
						if (newspawn.equals(oldpos)) {
							return;
						}
					}
					
					if (CampfireSpawnandTweaksUtil.checkForCampfireSpawnRemoval(world, playername, oldpos)) {
						if (CampfireSpawnandTweaksConfigHandler.GENERAL.sendMessageOnCampfireSpawnOverride.get()) {
							StringFunctions.sendMessage(player, "Campfire spawn point unset.", TextFormatting.DARK_GRAY);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onCampfireBreak(BlockEvent.BreakEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockPos pos = e.getPos();
		if (world.getBlockState(pos).getBlock() instanceof CampfireBlock) {
			PlayerEntity player = e.getPlayer();
			String playername = player.getName().getString().toLowerCase();

			if (CampfireSpawnandTweaksUtil.checkForCampfireSpawnRemoval(world, playername, pos)) {
				if (CampfireSpawnandTweaksConfigHandler.GENERAL.sendMessageOnNewCampfireSpawnSet.get()) {
					StringFunctions.sendMessage(player, "Campfire spawn point removed.", TextFormatting.DARK_GRAY);
				}
			}
		}
		
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		PlayerEntity player = e.getPlayer();
		World world = player.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		String playername = player.getName().getString().toLowerCase();
		if (!playercampfires.containsKey(playername)) {
			return;
		}
		
		Pair<World, BlockPos> pair = playercampfires.get(playername);
		playerstorespawn.get(pair.getFirst()).add(new Pair<PlayerEntity, BlockPos>(player, pair.getSecond().toImmutable()));
	}
}