/*
 * This is the latest source code of Mineral Chance.
 * Minecraft version: 1.17.x, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Mineral Chance ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.mineralchance.events;

import com.natamus.collective_fabric.data.GlobalVariables;
import com.natamus.collective_fabric.fakeplayer.FakePlayer;
import com.natamus.collective_fabric.functions.CompareBlockFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.collective_fabric.functions.WorldFunctions;
import com.natamus.mineralchance.config.ConfigHandler;
import com.natamus.mineralchance.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MiningEvent {
	public static void onBlockBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return;
		}
		
		if (ConfigHandler.ignoreFakePlayers.getValue()) {
			if (player instanceof FakePlayer) {
				return;
			}
		}
		
		if (player.isCreative()) {
			return;
		}
		
		Block block = state.getBlock();
		if (!CompareBlockFunctions.isStoneBlock(block) && !CompareBlockFunctions.isNetherStoneBlock(block)) {
			return;
		}
		
		Item randommineral;
		if (WorldFunctions.isOverworld(world)) {
			if (!ConfigHandler.enableOverworldMinerals.getValue()) {
				return;
			}
			if (CompareBlockFunctions.isNetherStoneBlock(block)) {
				return;
			}
			if (GlobalVariables.random.nextDouble() > ConfigHandler.extraMineralChanceOnOverworldStoneBreak.getValue()) {
				return;
			}
			randommineral = Util.getRandomOverworldMineral();
		}
		else if (WorldFunctions.isNether(world)) {
			if (!ConfigHandler.enableNetherMinerals.getValue()) {
				return;
			}
			if (!CompareBlockFunctions.isNetherStoneBlock(block)) {
				return;
			}
			if (GlobalVariables.random.nextDouble() > ConfigHandler.extraMineralChanceOnNetherStoneBreak.getValue()) {
				return;
			}
			randommineral = Util.getRandomNetherMineral();
		}
		else {
			return;
		}
		
		ItemEntity mineralentity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, new ItemStack(randommineral, 1));
		world.addFreshEntity(mineralentity);
		
		if (ConfigHandler.sendMessageOnMineralFind.getValue()) {
			StringFunctions.sendMessage(player, ConfigHandler.foundMineralMessage.getValue(), ChatFormatting.DARK_GREEN);
		}
	}
}
