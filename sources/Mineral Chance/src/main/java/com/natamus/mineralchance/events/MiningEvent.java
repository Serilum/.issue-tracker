/*
 * This is the latest source code of Mineral Chance.
 * Minecraft version: 1.17.1, mod version: 1.6.
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

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.CompareBlockFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MiningEvent {
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Player player = e.getPlayer();
		if (ConfigHandler.GENERAL.ignoreFakePlayers.get()) {
			if (player instanceof FakePlayer) {
				return;
			}
		}
		
		if (player.isCreative()) {
			return;
		}
		
		BlockState state = e.getState();
		Block block = state.getBlock();
		if (!CompareBlockFunctions.isStoneBlock(block) && !CompareBlockFunctions.isNetherStoneBlock(block)) {
			return;
		}
		
		Item randommineral;
		if (WorldFunctions.isOverworld(world)) {
			if (!ConfigHandler.GENERAL.enableOverworldMinerals.get()) {
				return;
			}
			if (CompareBlockFunctions.isNetherStoneBlock(block)) {
				return;
			}
			if (GlobalVariables.random.nextDouble() > ConfigHandler.GENERAL.extraMineralChanceOnOverworldStoneBreak.get()) {
				return;
			}
			randommineral = Util.getRandomOverworldMineral();
		}
		else if (WorldFunctions.isNether(world)) {
			if (!ConfigHandler.GENERAL.enableNetherMinerals.get()) {
				return;
			}
			if (!CompareBlockFunctions.isNetherStoneBlock(block)) {
				return;
			}
			if (GlobalVariables.random.nextDouble() > ConfigHandler.GENERAL.extraMineralChanceOnNetherStoneBreak.get()) {
				return;
			}
			randommineral = Util.getRandomNetherMineral();
		}
		else {
			return;
		}
		
		BlockPos pos = e.getPos();
		ItemEntity mineralentity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, new ItemStack(randommineral, 1));
		world.addFreshEntity(mineralentity);
		
		if (ConfigHandler.GENERAL.sendMessageOnMineralFind.get()) {
			StringFunctions.sendMessage(player, ConfigHandler.GENERAL.foundMineralMessage.get(), ChatFormatting.DARK_GREEN);
		}
	}
}
