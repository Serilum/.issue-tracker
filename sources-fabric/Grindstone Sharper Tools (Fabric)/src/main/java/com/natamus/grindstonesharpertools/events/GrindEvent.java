/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.grindstonesharpertools.events;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.ItemFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.grindstonesharpertools.config.ConfigHandler;
import com.natamus.grindstonesharpertools.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;

public class GrindEvent {
	public static float onDamage(Level world, Entity entity, DamageSource damageSource, float damageAmount) {
		Entity source = damageSource.getEntity();
		if (source == null) {
			return damageAmount;
		}
		
		if (world.isClientSide) {
			return damageAmount;
		}
		
		if (source instanceof Player == false) {
			return damageAmount;
		}
		
		Player player = (Player)source;
		ItemStack hand = player.getMainHandItem();
		
		if (ItemFunctions.isTool(hand)) {
			CompoundTag nbtc = hand.getOrCreateTag();
			if (nbtc.contains("sharper")) {
				int sharpleft = nbtc.getInt("sharper")-1;
				
				if (sharpleft > 0) {
					nbtc.putInt("sharper", sharpleft);
					double modifier = ConfigHandler.sharpenedDamageModifier.getValue();
					damageAmount *= (float)modifier;
					
					int totaluses = ConfigHandler.usesAfterGrinding.getValue();
					if ((double)sharpleft == (double)totaluses*0.75) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 75% of its uses left.", ChatFormatting.BLUE);
					}
					else if ((double)sharpleft == (double)totaluses*0.5) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 50% of its uses left.", ChatFormatting.BLUE);
					}
					else if ((double)sharpleft == (double)totaluses*0.25) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 25% of its uses left.", ChatFormatting.BLUE);
					}
					else if ((double)sharpleft == (double)totaluses*0.1) {
						StringFunctions.sendMessage(player, "Your sharpened tool has 10% of its uses left.", ChatFormatting.BLUE);
					}
				}
				else {
					nbtc.remove("sharper");
					StringFunctions.sendMessage(player, "Your tool is no longer sharpened.", ChatFormatting.RED);
				}
				hand.setTag(nbtc);
				Util.updateName(hand, sharpleft);
			}
		}
		
		return damageAmount;
	}
	
	public static InteractionResult onClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide || !hand.equals(InteractionHand.MAIN_HAND)) {
			return InteractionResult.PASS;
		}
		
		BlockPos pos = BlockPosFunctions.getBlockPosFromHitResult(hitResult);
		Block block = world.getBlockState(pos).getBlock();
		if (block.equals(Blocks.GRINDSTONE)) {
			if (player.isCrouching()) {
				ItemStack itemstack = player.getItemInHand(hand);
				if (ItemFunctions.isTool(itemstack)) {
					CompoundTag nbtc = itemstack.getOrCreateTag();
					int sharpeneduses = ConfigHandler.usesAfterGrinding.getValue();
					
					nbtc.putInt("sharper", sharpeneduses);
					itemstack.setTag(nbtc);
					Util.updateName(itemstack, sharpeneduses);
					StringFunctions.sendMessage(player, "Your tool has been sharpened with " + sharpeneduses + " uses.", ChatFormatting.DARK_GREEN);
					return InteractionResult.FAIL;
				}
			}
		}
		
		return InteractionResult.PASS;
	}
}
