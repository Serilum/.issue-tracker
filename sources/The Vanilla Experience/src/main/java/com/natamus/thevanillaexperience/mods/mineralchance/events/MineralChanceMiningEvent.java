/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.mineralchance.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.mineralchance.config.MineralChanceConfigHandler;
import com.natamus.thevanillaexperience.mods.mineralchance.util.MineralChanceUtil;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MineralChanceMiningEvent {
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Player player = e.getPlayer();
		if (MineralChanceConfigHandler.GENERAL.ignoreFakePlayers.get()) {
			if (player instanceof FakePlayer) {
				return;
			}
		}
		
		if (player.isCreative()) {
			return;
		}
		
		BlockState state = e.getState();
		Block block = state.getBlock();
		if (!MineralChanceUtil.isStoneBlock(block)) {
			return;
		}
		
		if (GlobalVariables.random.nextDouble() > MineralChanceConfigHandler.GENERAL.extraMineralChanceOnStoneBreak.get()) {
			return;
		}
		
		BlockPos pos = e.getPos();
		Item randommineral;
		if (WorldFunctions.isOverworld(world)) {
			if (!MineralChanceConfigHandler.GENERAL.enableOverworldMinerals.get()) {
				return;
			}
			if (MineralChanceUtil.isNetherStoneBlock(block)) {
				return;
			}
			randommineral = MineralChanceUtil.getRandomOverworldMineral();
		}
		else if (WorldFunctions.isNether(world)) {
			if (!MineralChanceConfigHandler.GENERAL.enableNetherMinerals.get()) {
				return;
			}
			if (!MineralChanceUtil.isNetherStoneBlock(block)) {
				return;
			}
			randommineral = MineralChanceUtil.getRandomNetherMineral();
		}
		else {
			return;
		}
		
		ItemEntity mineralentity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, new ItemStack(randommineral, 1));
		world.addFreshEntity(mineralentity);
		
		if (MineralChanceConfigHandler.GENERAL.sendMessageOnMineralFind.get()) {
			StringFunctions.sendMessage(player, MineralChanceConfigHandler.GENERAL.foundMineralMessage.get(), ChatFormatting.DARK_GREEN);
		}
	}
}
