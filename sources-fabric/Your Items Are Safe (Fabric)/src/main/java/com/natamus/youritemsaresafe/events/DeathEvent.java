/*
 * This is the latest source code of Your Items Are Safe.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.youritemsaresafe.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective_fabric.functions.CompareBlockFunctions;
import com.natamus.collective_fabric.functions.DataFunctions;
import com.natamus.collective_fabric.functions.HeadFunctions;
import com.natamus.collective_fabric.functions.TileEntityFunctions;
import com.natamus.youritemsaresafe.config.ConfigHandler;
import com.natamus.youritemsaresafe.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DeathEvent {
	private static final List<EquipmentSlot> slottypes = new ArrayList<EquipmentSlot>(Arrays.asList(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));
	
	public static void onPlayerDeath(ServerPlayer player, DamageSource damageSource, float damageAmount) {
		Level world = player.level;
		
		int chestcount = 1;
		String playername = player.getName().getString();
		
		List<ItemStack> itemstacks = new ArrayList<>(player.getInventory().items);
		
		int totalitemcount = 0;
		for (ItemStack itemstack : itemstacks) {
			if (!itemstack.isEmpty()) {
				totalitemcount += 1;
			}
		}
		
		if (!ConfigHandler.createArmorStand.getValue()) {
			for (EquipmentSlot slottype : slottypes) {
				if (!player.getItemBySlot(slottype).isEmpty()) {
					totalitemcount += 1;
				}
			}
			
			if (!player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
				totalitemcount += 1;
			}
		}
		
		if (totalitemcount == 0) {
			return;
		}
		
		if (ConfigHandler.needChestMaterials.getValue() || ConfigHandler.needArmorStandMaterials.getValue() || ConfigHandler.needSignMaterials.getValue()) {			
			if (ConfigHandler.createArmorStand.getValue() && ConfigHandler.addPlayerHeadToArmorStand.getValue() && !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
				totalitemcount += 1;
			}
			
			int stoneleft = 1; // 1 armor stand
			int planksleft = 0; // 1 chest, 1 armor stand
			
			if (ConfigHandler.needChestMaterials.getValue()) {
				planksleft += 8;
				if (totalitemcount > 27) {
					planksleft += 8;
				}
			}
			
			if (ConfigHandler.createArmorStand.getValue() && ConfigHandler.needArmorStandMaterials.getValue()) {
				planksleft += 3;
			}
			
			if (ConfigHandler.createSignWithPlayerName.getValue() && ConfigHandler.needSignMaterials.getValue()) {
				planksleft += 7;
			}
			
			if (ConfigHandler.ignoreStoneMaterialNeed.getValue()) {
				stoneleft = 0;
			}
			
			int planksneeded = planksleft;
			int stoneneeded = stoneleft;
			
			planksleft = Util.processLogCheck(itemstacks, planksleft);
			
			if (planksleft > 0) {
				planksleft = Util.processPlankCheck(itemstacks, planksleft);
			}
			if (planksleft > 0) {
				planksleft = Util.processChestCheck(itemstacks, planksleft);
			}
			
			if (planksleft > 0) {
				Util.failureMessage(player, planksleft, stoneleft, planksneeded, stoneneeded);
				return;
			}
			
			if (stoneleft > 0) {
				stoneleft = Util.processStoneCheck(itemstacks, stoneleft);
			}
			if (stoneleft > 0) {
				stoneleft = Util.processSlabCheck(itemstacks, stoneleft);
			}
			
			if (stoneleft > 0) {
				Util.failureMessage(player, planksleft, stoneleft, planksneeded, stoneneeded);
				return;
			}
		}
		
		BlockPos deathpos = player.blockPosition().immutable();
		if (CompareBlockFunctions.isAirOrOverwritableBlock(world.getBlockState(deathpos.below()).getBlock())) {
			deathpos = deathpos.below().immutable();
		}
	
		ArmorStand armorstand = null;
		
		if (ConfigHandler.createArmorStand.getValue()) {
			armorstand = new ArmorStand(EntityType.ARMOR_STAND, world);
	
			for (EquipmentSlot slottype : slottypes) {
				armorstand.setItemSlot(slottype, player.getItemBySlot(slottype).copy());
				player.setItemSlot(slottype, ItemStack.EMPTY);
			}
			
			itemstacks = new ArrayList<>(player.getInventory().items);
			
			if (ConfigHandler.addPlayerHeadToArmorStand.getValue()) {
				ItemStack headstack = HeadFunctions.getPlayerHead(playername, 1);
				
				if (headstack != null) {
					if (!player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
						itemstacks.add(player.getItemBySlot(EquipmentSlot.HEAD).copy());
						player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
					}
					
					armorstand.setItemSlot(EquipmentSlot.HEAD, headstack);
				}
			}
		}
		else {
			for (EquipmentSlot slottype : slottypes) {
				itemstacks.add(player.getItemBySlot(slottype).copy());
				player.setItemSlot(slottype, ItemStack.EMPTY);
			}
			
			itemstacks.add(player.getItemBySlot(EquipmentSlot.HEAD).copy());
			player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
		}
		
		BlockState cheststate = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
		ChestBlockEntity chestentity = new ChestBlockEntity(deathpos, cheststate);
		world.setBlock(deathpos, cheststate, 3);
		world.setBlockEntity(chestentity);
		
		BlockPos deathposup = new BlockPos(deathpos.getX(), deathpos.getY()+1, deathpos.getZ());
		ChestBlockEntity chestentitytwo = new ChestBlockEntity(deathposup, cheststate);
		
		int i = 0;
		for (ItemStack itemstack : itemstacks) {
			if (itemstack.isEmpty()) {
				continue;
			}
			
			if (i < 27) {
				chestentity.setItem(i, itemstack.copy());
				itemstack.setCount(0);
			}
			else if (i >= 27) {
				if (chestcount == 1) {
					chestcount+=1;
					world.setBlock(deathposup, cheststate, 3);
					world.setBlockEntity(chestentitytwo);
				}
				
				chestentitytwo.setItem(i-27, itemstack.copy());
				itemstack.setCount(0);
			}
			
			i+=1;
		}
		
		if (armorstand != null) {
			armorstand.setPos(deathpos.getX()+0.5, deathpos.getY()+chestcount, deathpos.getZ()+0.5);
			armorstand.getEntityData().set(ArmorStand.DATA_CLIENT_FLAGS, DataFunctions.setBit(armorstand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS), 4, true));
			world.addFreshEntity(armorstand);
		}
		
		Util.successMessage(player);
		
		if (ConfigHandler.createSignWithPlayerName.getValue()) {
			BlockPos signpos = deathpos.south().immutable();
			world.setBlockAndUpdate(signpos, Blocks.OAK_WALL_SIGN.defaultBlockState().setValue(WallSignBlock.FACING, Direction.SOUTH));
			
			BlockEntity te = world.getBlockEntity(signpos);
			if (te instanceof SignBlockEntity == false) {
				return;
			}
			
			SignBlockEntity signentity = (SignBlockEntity)te;
			signentity.setMessage(1, Component.literal(playername));
			TileEntityFunctions.updateTileEntity(world, signpos, signentity);
		}
	}
}
