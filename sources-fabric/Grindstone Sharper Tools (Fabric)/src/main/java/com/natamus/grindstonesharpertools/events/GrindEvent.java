/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.2, mod version: 2.2.
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
import net.minecraft.world.phys.BlockHitResult;
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

		if (!(source instanceof Player)) {
			return damageAmount;
		}

		Player player = (Player) source;
		ItemStack hand = player.getMainHandItem();

		if (ItemFunctions.isTool(hand)) {
			CompoundTag nbtc = hand.getOrCreateTag();
			if (nbtc.contains("sharper")) {
				int sharpLeft = nbtc.getInt("sharper");
				if (!player.isCreative() || !ConfigHandler.infiniteCreativeUses.getValue()) {
					sharpLeft--;
				}

				if (sharpLeft > 0) {
					nbtc.putInt("sharper", sharpLeft);
					double modifier = ConfigHandler.sharpenedDamageModifier.getValue();
					damageAmount *= (float) modifier;

					if (ConfigHandler.sendUsesLeftInChat.getValue()) {
						int totalUses = ConfigHandler.usesAfterGrinding.getValue();
						if ((double) sharpLeft == (double) totalUses * 0.75) {
							StringFunctions.sendMessage(player, "Your sharpened tool has 75% of its uses left.", ChatFormatting.BLUE);
						}
						else if ((double) sharpLeft == (double) totalUses * 0.5) {
							StringFunctions.sendMessage(player, "Your sharpened tool has 50% of its uses left.", ChatFormatting.BLUE);
						}
						else if ((double) sharpLeft == (double) totalUses * 0.25) {
							StringFunctions.sendMessage(player, "Your sharpened tool has 25% of its uses left.", ChatFormatting.BLUE);
						}
						else if ((double) sharpLeft == (double) totalUses * 0.1) {
							StringFunctions.sendMessage(player, "Your sharpened tool has 10% of its uses left.", ChatFormatting.BLUE);
						}
					}
				}
				else {
					nbtc.remove("sharper");
					StringFunctions.sendMessage(player, "Your tool is no longer sharpened.", ChatFormatting.RED);
				}
				hand.setTag(nbtc);
				Util.updateName(hand, sharpLeft);
			}
		}

		return damageAmount;
	}

	public static boolean onClick(Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
		if (world.isClientSide || !hand.equals(InteractionHand.MAIN_HAND)) {
			return true;
		}

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
					return false;
				}
			}
		}

		return true;
	}
}