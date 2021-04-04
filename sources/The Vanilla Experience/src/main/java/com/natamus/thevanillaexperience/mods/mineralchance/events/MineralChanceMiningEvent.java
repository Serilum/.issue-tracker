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

package com.natamus.thevanillaexperience.mods.mineralchance.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.mineralchance.config.MineralChanceConfigHandler;
import com.natamus.thevanillaexperience.mods.mineralchance.util.MineralChanceUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MineralChanceMiningEvent {
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
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
		world.addEntity(mineralentity);
		
		if (MineralChanceConfigHandler.GENERAL.sendMessageOnMineralFind.get()) {
			StringFunctions.sendMessage(player, MineralChanceConfigHandler.GENERAL.foundMineralMessage.get(), TextFormatting.DARK_GREEN);
		}
	}
}
