/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.16.5, mod version: 2.27.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.util.Reference;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PlayerFunctions {
	public static boolean respawnPlayer(World world, PlayerEntity player) {
		if (player instanceof ServerPlayerEntity == false) {
			return false;
		}
		
		MinecraftServer server = world.getServer();
		ServerPlayerEntity serverplayer = (ServerPlayerEntity)player;
		
		if (serverplayer.wonGame) {
			serverplayer.wonGame = false;
			serverplayer = server.getPlayerList().respawn(serverplayer, true);
			CriteriaTriggers.CHANGED_DIMENSION.trigger(serverplayer, World.END, World.OVERWORLD);
		}
		else if (serverplayer.getHealth() <= 0.0F) {
			serverplayer = server.getPlayerList().respawn(serverplayer, false);
		}
		
		return true;
	}
	
	public static PlayerEntity matchPlayer(PlayerEntity player, String other) {
		return matchPlayer(player.getCommandSenderWorld(), other);
	}
	public static PlayerEntity matchPlayer(World world, String other) {
		List<? extends PlayerEntity> players = world.players();

		for (PlayerEntity onlineplayer : players) {
			if (onlineplayer.getName().getString().toLowerCase().equals(other)) {
				return onlineplayer;
			}
		}
		return null;
	}
	
	public static boolean isHoldingWater(PlayerEntity player) {
		if (player.getItemInHand(Hand.OFF_HAND).getItem().equals(Items.WATER_BUCKET) || player.getItemInHand(Hand.MAIN_HAND).getItem().equals(Items.WATER_BUCKET)) {
			return true;
		}
		return false;
	}
	
	public static boolean isJoiningWorldForTheFirstTime(PlayerEntity player, String modid) {
		String firstjointag = Reference.MOD_ID + ".firstJoin." + modid;
		
		Set<String> tags = player.getTags();
		if (tags.contains(firstjointag)) {
			return false;
		}
		
		player.addTag(firstjointag);
		
		PlayerInventory inv = player.inventory;
		boolean isempty = true;
		for (int i=0; i < 36; i++) {
			if (!inv.getItem(i).isEmpty()) {
				isempty = false;
				break;
			}
		}
		
		if (!isempty) {
			return false;
		}
		
		
		World world = player.getCommandSenderWorld();
		ServerWorld serverworld = (ServerWorld)world;
		BlockPos wspos = serverworld.getSharedSpawnPos();
		BlockPos ppos = player.blockPosition();
		BlockPos cpos = new BlockPos(ppos.getX(), wspos.getY(), ppos.getZ());
		
		if (cpos.closerThan(wspos, 50)) {
			return true;
		}
		return false;
	}
	
	public static BlockPos getSpawnPoint(World world, PlayerEntity player) {
		Vector3d spawnvec = getSpawnVec(world, player);
		return new BlockPos(spawnvec.x, spawnvec.y, spawnvec.z);
	}
	public static Vector3d getSpawnVec(World world, PlayerEntity player) {
		ServerPlayerEntity serverplayer = (ServerPlayerEntity)player;
		ServerWorld serverworld = (ServerWorld)world;
		
		BlockPos respawnlocation = serverworld.getSharedSpawnPos(); // get spawn point
		Vector3d respawnvec = new Vector3d(respawnlocation.getX(), respawnlocation.getY(), respawnlocation.getZ());
		
		BlockPos bedpos = serverplayer.getRespawnPosition();
		if (bedpos != null) { 
			Optional<Vector3d> optionalbed = PlayerEntity.findRespawnPositionAndUseSpawnBlock(serverworld, bedpos, 1.0f, false, false);
			if (optionalbed != null) {
				if (optionalbed.isPresent()) {
					Vector3d bedvec = optionalbed.get();
					BlockPos bp = new BlockPos(bedvec);
					Iterator<BlockPos> it = BlockPos.betweenClosedStream(bp.getX()-1, bp.getY()-1, bp.getZ()-1, bp.getX()+1, bp.getY()+1, bp.getZ()+1).iterator();
					while (it.hasNext()) {
						BlockPos np = it.next();
						BlockState state = world.getBlockState(np);
						Block block = state.getBlock();
						if (block instanceof BedBlock) { // Found bed
							respawnvec = bedvec;
							break;
						}
					}
				}
			}
		}
		
		return respawnvec;
	}
	
	
	// Player Gear Functions
	public static String getPlayerGearString(PlayerEntity player) {
		String skconfig = "";
		
		ItemStack offhand = player.getItemBySlot(EquipmentSlotType.OFFHAND);
		if (!offhand.isEmpty()) {
			CompoundNBT nbt = new CompoundNBT();
			nbt = offhand.save(nbt);
			String nbtstring = nbt.toString();
			skconfig += "'offhand'" + " : " + "'" + nbtstring + "',";
		}
		else {
			skconfig += "'offhand'" + " : " + "'',";
		}
		
		ItemStack head = player.getItemBySlot(EquipmentSlotType.HEAD);
		if (!head.isEmpty()) {
			CompoundNBT nbt = new CompoundNBT();
			nbt = head.save(nbt);
			String nbtstring = nbt.toString();
			skconfig += "\n" + "'head'" + " : " + "'" + nbtstring + "',";
		}
		else {
			skconfig += "\n" + "'head'" + " : " + "'',";
		}
		
		ItemStack chest = player.getItemBySlot(EquipmentSlotType.CHEST);
		if (!chest.isEmpty()) {
			CompoundNBT nbt = new CompoundNBT();
			nbt = chest.save(nbt);
			String nbtstring = nbt.toString();
			skconfig += "\n" + "'chest'" + " : " + "'" + nbtstring + "',";
		}
		else {
			skconfig += "\n" + "'chest'" + " : " + "'',";
		}
		
		ItemStack legs = player.getItemBySlot(EquipmentSlotType.LEGS);
		if (!legs.isEmpty()) {
			CompoundNBT nbt = new CompoundNBT();
			nbt = legs.save(nbt);
			String nbtstring = nbt.toString();
			skconfig += "\n" + "'legs'" + " : " + "'" + nbtstring + "',";
		}
		else {
			skconfig += "\n" + "'legs'" + " : " + "'',";
		}
		
		ItemStack feet = player.getItemBySlot(EquipmentSlotType.FEET);
		if (!feet.isEmpty()) {
			CompoundNBT nbt = new CompoundNBT();
			nbt = feet.save(nbt);
			String nbtstring = nbt.toString();
			skconfig += "\n" + "'feet'" + " : " + "'" + nbtstring + "',";
		}
		else {
			skconfig += "\n" + "'feet'" + " : " + "'',";
		}
		
		PlayerInventory inv = player.inventory;
		for (int i=0; i < 36; i++) {
			ItemStack slot = inv.getItem(i);
			if (!slot.isEmpty()) {
				CompoundNBT nbt = new CompoundNBT();
				nbt = slot.save(nbt);
				String nbtstring = nbt.toString();
				skconfig += "\n" + i + " : " + "'" + nbtstring + "',";
			}
			else {
				skconfig += "\n" + i + " : '',";
			}
		}
		
		return skconfig;
	}
	
	public static String getPlayerGearStringFromHashMap(HashMap<String, ItemStack> gear) {
		String gearstring = "";
		
		List<String> specialslots = new ArrayList<String>(Arrays.asList("offhand", "head", "chest", "legs", "feet"));
		for (String specialslot : specialslots) {
			String specialslotstring = "";
			if (gear.containsKey(specialslot)) {
				CompoundNBT nbt = new CompoundNBT();
				nbt = gear.get(specialslot).save(nbt);
				specialslotstring = nbt.toString();
			}
			if (gearstring != "") {
				gearstring += "\n";
			}
			gearstring += "'" + specialslot + "'" + " : " + "'" + specialslotstring + "',";
		}
		
		List<ItemStack> emptyinventory = NonNullList.withSize(36, ItemStack.EMPTY);
		for (int i = 0; i < emptyinventory.size(); i++) {
			String itemstring = "";
			if (gear.containsKey("" + i)) {
				CompoundNBT nbt = new CompoundNBT();
				nbt = gear.get("" + i).save(nbt);
				itemstring = nbt.toString();
			}
			gearstring += "\n" + i + " : '" + itemstring + "',";
		}
		
		return gearstring;
	}
	
	public static void setPlayerGearFromString(PlayerEntity player, String gearconfig) {
		String[] gearspl = gearconfig.split("\n");
		int newlinecount = gearspl.length;
		if (newlinecount < 40) {
			System.out.println("[Error] setPlayerGearFromString: The gear config does not contain 40 lines and is invalid.");
			return;
		}
		
		boolean cleared = false;
		for (String line : gearspl) {
			line = line.trim().replaceAll("\n", "");
			if (line.endsWith(",")) {
				line = line.substring(0, line.length() - 1);
			}
			
			String[] lspl = line.split(" : ");
			if (lspl.length != 2) {
				System.out.println("[Error] setPlayerGearFromString: The line '" + line + "' is invalid.");
				return;
			}
			
			String slotstring = lspl[0].replace("'", "");
			String data = lspl[1];
			data = data.substring(1, data.length() - 1);
			if (data.length() < 2) {
				continue;
			}
			
			ItemStack itemstack = null;
			try {
				CompoundNBT newnbt = JsonToNBT.parseTag(data);
				itemstack = ItemStack.of(newnbt);
			} catch (CommandSyntaxException e) {}
			
			if (itemstack == null) {
				System.out.println("[Error] setPlayerGearFromString: Unable to get the correct itemstack data from '" + line + "'.");
				return;				
			}
			
			if (!cleared) {
				cleared = true;
				player.inventory.clearContent();
			}
			
			if (NumberFunctions.isNumeric(slotstring)) {
				int slot = Integer.parseInt(slotstring);
				player.inventory.setItem(slot, itemstack);
				continue;
			}
			
			EquipmentSlotType type = null;
			if (slotstring.equals("offhand")) {
				type = EquipmentSlotType.OFFHAND;
			}
			else if (slotstring.equals("head")) {
				type = EquipmentSlotType.HEAD;
			}
			else if (slotstring.equals("chest")) {
				type = EquipmentSlotType.CHEST;
			}
			else if (slotstring.equals("legs")) {
				type = EquipmentSlotType.LEGS;
			}
			else if (slotstring.equals("feet")) {
				type = EquipmentSlotType.FEET;
			}
			else {
				continue;
			}

			if (type == null) {
				continue;
			}
			
			player.setItemSlot(type, itemstack);
		}
	}
}
