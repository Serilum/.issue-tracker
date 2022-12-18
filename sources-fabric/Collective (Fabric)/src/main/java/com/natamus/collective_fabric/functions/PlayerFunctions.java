/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.43.
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

package com.natamus.collective_fabric.functions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective_fabric.util.Reference;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class PlayerFunctions {
	public static boolean respawnPlayer(Level world, Player player) {
		if (!(player instanceof ServerPlayer)) {
			return false;
		}
		
		MinecraftServer server = world.getServer();
		ServerPlayer serverplayer = (ServerPlayer)player;
		
		if (serverplayer.wonGame) {
			serverplayer.wonGame = false;
			serverplayer = server.getPlayerList().respawn(serverplayer, true);
			CriteriaTriggers.CHANGED_DIMENSION.trigger(serverplayer, Level.END, Level.OVERWORLD);
		}
		else if (serverplayer.getHealth() <= 0.0F) {
			server.getPlayerList().respawn(serverplayer, false);
		}
		
		return true;
	}
	
	public static Player matchPlayer(Player player, String other) {
		return matchPlayer(player.getCommandSenderWorld(), other);
	}
	public static Player matchPlayer(Level world, String other) {
		List<? extends Player> players = world.players();

		for (Player onlineplayer : players) {
			if (onlineplayer.getName().getString().toLowerCase().equals(other)) {
				return onlineplayer;
			}
		}
		return null;
	}
	
	public static boolean isHoldingWater(Player player) {
		return player.getItemInHand(InteractionHand.OFF_HAND).getItem().equals(Items.WATER_BUCKET) || player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.WATER_BUCKET);
	}
	
	public static boolean isJoiningWorldForTheFirstTime(Player player, String modid) {
		return isJoiningWorldForTheFirstTime(player, modid, true);
	}
	public static boolean isJoiningWorldForTheFirstTime(Player player, String modid, boolean mustHaveEmptyInventory) {
		String firstjointag = Reference.MOD_ID + ".firstJoin." + modid;
		
		Set<String> tags = player.getTags();
		if (tags.contains(firstjointag)) {
			return false;
		}
		
		player.addTag(firstjointag);

		if (mustHaveEmptyInventory) {
			Inventory inv = player.getInventory();
			boolean isempty = true;
			for (int i = 0; i < 36; i++) {
				if (!inv.getItem(i).isEmpty()) {
					isempty = false;
					break;
				}
			}

			if (!isempty) {
				return false;
			}
		}
		
		Level world = player.getCommandSenderWorld();
		ServerLevel ServerLevel = (ServerLevel)world;
		BlockPos wspos = ServerLevel.getSharedSpawnPos();
		BlockPos ppos = player.blockPosition();
		BlockPos cpos = new BlockPos(ppos.getX(), wspos.getY(), ppos.getZ());

		return cpos.closerThan(wspos, 50);
	}
	
	public static BlockPos getSpawnPoint(Level world, Player player) {
		Vec3 spawnvec = getSpawnVec(world, player);
		return new BlockPos(spawnvec.x, spawnvec.y, spawnvec.z);
	}
	public static Vec3 getSpawnVec(Level world, Player player) {
		ServerPlayer serverplayer = (ServerPlayer)player;
		ServerLevel ServerLevel = (ServerLevel)world;
		
		BlockPos respawnlocation = ServerLevel.getSharedSpawnPos(); // get spawn point
		Vec3 respawnvec = new Vec3(respawnlocation.getX(), respawnlocation.getY(), respawnlocation.getZ());
		
		BlockPos bedpos = serverplayer.getRespawnPosition();
		if (bedpos != null) {
			Optional<Vec3> optionalbed = Player.findRespawnPositionAndUseSpawnBlock(ServerLevel, bedpos, 1.0f, false, false);
			if (optionalbed.isPresent()) {
				Vec3 bedvec = optionalbed.get();
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
		
		return respawnvec;
	}
	
	public static InteractionHand getOtherHand(InteractionHand hand) {
		if (hand.equals(InteractionHand.MAIN_HAND)) {
			return InteractionHand.OFF_HAND;
		}
		return InteractionHand.MAIN_HAND;
	}
	
	// Player Gear Functions
	public static String getPlayerGearString(Player player) {
		StringBuilder skconfig = new StringBuilder();
		
		ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
		if (!offhand.isEmpty()) {
			String nbtstring = ItemFunctions.getNBTStringFromItemStack(offhand);
			skconfig.append("'offhand'" + " : " + "'").append(nbtstring).append("',");
		}
		else {
			skconfig.append("'offhand'" + " : " + "'',");
		}

		ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!head.isEmpty()) {
			String nbtstring = ItemFunctions.getNBTStringFromItemStack(head);
			skconfig.append("\n" + "'head'" + " : " + "'").append(nbtstring).append("',");
		}
		else {
			skconfig.append("\n" + "'head'" + " : " + "'',");
		}

		ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
		if (!chest.isEmpty()) {
			String nbtstring = ItemFunctions.getNBTStringFromItemStack(chest);
			skconfig.append("\n" + "'chest'" + " : " + "'").append(nbtstring).append("',");
		}
		else {
			skconfig.append("\n" + "'chest'" + " : " + "'',");
		}

		ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
		if (!legs.isEmpty()) {
			String nbtstring = ItemFunctions.getNBTStringFromItemStack(legs);
			skconfig.append("\n" + "'legs'" + " : " + "'").append(nbtstring).append("',");
		}
		else {
			skconfig.append("\n" + "'legs'" + " : " + "'',");
		}

		ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
		if (!feet.isEmpty()) {
			String nbtstring = ItemFunctions.getNBTStringFromItemStack(feet);
			skconfig.append("\n" + "'feet'" + " : " + "'").append(nbtstring).append("',");
		}
		else {
			skconfig.append("\n" + "'feet'" + " : " + "'',");
		}

		Inventory inv = player.getInventory();
		for (int i=0; i < 36; i++) {
			ItemStack slot = inv.getItem(i);
			if (!slot.isEmpty()) {
				String nbtstring = ItemFunctions.getNBTStringFromItemStack(slot);
				skconfig.append("\n").append(i).append(" : ").append("'").append(nbtstring).append("',");
			}
			else {
				skconfig.append("\n").append(i).append(" : '',");
			}
		}

		return skconfig.toString();
	}

	public static String getPlayerGearStringFromHashMap(HashMap<String, ItemStack> gear) {
		StringBuilder gearstring = new StringBuilder();

		List<String> specialslots = new ArrayList<String>(Arrays.asList("offhand", "head", "chest", "legs", "feet"));
		for (String specialslot : specialslots) {
			String specialslotstring = "";
			if (gear.containsKey(specialslot)) {
				specialslotstring = ItemFunctions.getNBTStringFromItemStack(gear.get(specialslot));
			}
			if (!gearstring.toString().equals("")) {
				gearstring.append("\n");
			}
			gearstring.append("'").append(specialslot).append("'").append(" : ").append("'").append(specialslotstring).append("',");
		}

		List<ItemStack> emptyinventory = NonNullList.withSize(36, ItemStack.EMPTY);
		for (int i = 0; i < emptyinventory.size(); i++) {
			String itemstring = "";
			if (gear.containsKey("" + i)) {
				itemstring = ItemFunctions.getNBTStringFromItemStack(gear.get("" + i));
			}
			gearstring.append("\n").append(i).append(" : '").append(itemstring).append("',");
		}
		
		return gearstring.toString();
	}
	
	public static void setPlayerGearFromString(Player player, String gearconfig) {
		String[] gearspl = gearconfig.split("',[\\r\\n]+");
		int newlinecount = gearspl.length;
		
		if (newlinecount < 40) {
			System.out.println("[Error] (Collective) setPlayerGearFromString: The gear config does not contain 40 lines and is invalid.");
			return;
		}
		
		boolean cleared = false;
		for (String line : gearspl) {
			line = line.trim();
			if (line.endsWith(",")) {
				line = line.substring(0, line.length() - 1);
			}
			if (!line.endsWith("'")) {
				line = line + "'";
			}
			
			String[] lspl = line.split(" : ");
			if (lspl.length != 2) {
				System.out.println("[Error] (Collective) setPlayerGearFromString: The line " + line + " is invalid.");
				return;
			}
			
			String slotstring = lspl[0].replace("'", "");
			String data = lspl[1];
			if (data.startsWith("'")) {
				data = data.substring(1);
			}
			if (data.endsWith("'")) {
				data = data.substring(0, data.length() - 1);
			}
			
			if (data.length() < 2) {
				continue;
			}
			
			data = data.replaceAll("\r", "");
			
			ItemStack itemstack = null;
			try {
				CompoundTag newnbt = TagParser.parseTag(data);
				itemstack = ItemStack.of(newnbt);
			} catch (CommandSyntaxException e0) {
				try {
					data = data.replace("\",\"", "|||,|||").replace(" \"", " '").replace("\" ", "' ").replace("|||,|||", "\",\"");
					CompoundTag newnbt = TagParser.parseTag(data);
					itemstack = ItemStack.of(newnbt);
				} catch (CommandSyntaxException e1) {
					e1.printStackTrace();
				}
			}
			
			if (itemstack == null) {
				System.out.println("[Error] (Collective) setPlayerGearFromString: Unable to get the correct itemstack data from data " + data);
				return;				
			}
			
			if (!cleared) {
				cleared = true;
				player.getInventory().clearContent();
			}
			
			if (NumberFunctions.isNumeric(slotstring)) {
				int slot = Integer.parseInt(slotstring);
				player.getInventory().setItem(slot, itemstack);
				continue;
			}
			
			EquipmentSlot type;
			switch (slotstring) {
				case "offhand" -> type = EquipmentSlot.OFFHAND;
				case "head" -> type = EquipmentSlot.HEAD;
				case "chest" -> type = EquipmentSlot.CHEST;
				case "legs" -> type = EquipmentSlot.LEGS;
				case "feet" -> type = EquipmentSlot.FEET;
				default -> {
					continue;
				}
			}

			player.setItemSlot(type, itemstack);
		}
	}
}
