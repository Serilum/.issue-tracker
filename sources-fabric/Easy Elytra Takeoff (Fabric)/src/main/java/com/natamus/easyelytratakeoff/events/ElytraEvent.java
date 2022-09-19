/*
 * This is the latest source code of Easy Elytra Takeoff.
 * Minecraft version: 1.19.2, mod version: 3.1.
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

package com.natamus.easyelytratakeoff.events;

import com.natamus.collective_fabric.functions.EntityFunctions;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;

public class ElytraEvent {
	private static HashMap<String, Integer> newrockets = new HashMap<String, Integer>();
	private static HashMap<String, InteractionHand> playerhands = new HashMap<String, InteractionHand>();

	public static void onPlayerTick(Level level, Player player) {
		if (level.isClientSide) {
			return;
		}

		String playerName = player.getName().getString();
		if (newrockets.containsKey(playerName)) {
			int left = newrockets.get(playerName);
			if (left > -5) {
				if (left > 0) {
					EntityFunctions.setEntityFlag(player, 7, true);
					FireworkRocketEntity efr = new FireworkRocketEntity(level, player.getItemInHand(InteractionHand.MAIN_HAND), player);
					level.addFreshEntity(efr);

					InteractionHand hand = playerhands.get(playerName);
					if (hand != null) {
						ItemStack handstack = player.getItemInHand(hand);
						String handname = handstack.getDescriptionId().toLowerCase();
						if ((handname.contains("booster_")) && !handname.contains("_empty")) { // Elytra Booster compatibility
							handstack.use(level, player, hand);
						}
						playerhands.remove(playerName);
					}
				}

				EntityFunctions.setEntityFlag(player, 7, true);
				newrockets.put(playerName, left-1);
			}
		}
	}

	public static InteractionResultHolder<ItemStack> onFirework(Player player, Level world, InteractionHand hand) {
		ItemStack handstack = player.getItemInHand(hand);
		if (world.isClientSide) {
			return InteractionResultHolder.pass(handstack);
		}

		boolean isbooster = false;
		Item item = handstack.getItem();
		String itemname = item.getDescriptionId().toLowerCase();

		if (!handstack.getItem().equals(Items.FIREWORK_ROCKET) ) {
			if (!((itemname.contains("booster_")) && !itemname.contains("_empty"))) { // Elytra Booster compatibility
				return InteractionResultHolder.pass(handstack);
			}
			isbooster = true;
		}
		
		if (player.isFallFlying()) {
			return InteractionResultHolder.pass(handstack);
		}

		BlockPos belowpos = player.blockPosition().below();

		boolean inAir = true;
		for (BlockPos next : BlockPos.betweenClosed(belowpos.getX() - 1, belowpos.getY() - 1, belowpos.getZ() - 1, belowpos.getX() + 1, belowpos.getY() - 1, belowpos.getZ() + 1)) {
			Block nextblock = world.getBlockState(next).getBlock();
			if (!nextblock.equals(Blocks.AIR)) {
				inAir = false;
				break;
			}
		}

		if (inAir) {
			return InteractionResultHolder.pass(handstack);
		}
		
		boolean foundelytra = EntityElytraEvents.CUSTOM.invoker().useCustomElytra(player, false);
		if (!foundelytra) {
			for (ItemStack nis : player.getArmorSlots()) {
				if (nis.getItem() instanceof ElytraItem) {
					foundelytra = true;
					break;
				}
			}

			if (!foundelytra) {
				return InteractionResultHolder.pass(handstack);
			}
		}

		String playerName = player.getName().getString();
		player.teleportTo(player.getX(), player.getY()+0.2, player.getZ());

		newrockets.put(playerName, 1);
		playerhands.put(playerName, hand);

		if (!player.isCreative() && !isbooster) {
			handstack.shrink(1);
		}
		
		return InteractionResultHolder.fail(handstack);
	}
}