/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
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

package com.natamus.thevanillaexperience.mods.betterbeaconplacement.events;

import com.natamus.thevanillaexperience.mods.betterbeaconplacement.config.BetterBeaconPlacementConfigHandler;
import com.natamus.thevanillaexperience.mods.betterbeaconplacement.util.BetterBeaconPlacementUtil;
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BetterBeaconPlacementBeaconEvent {
	@SubscribeEvent
	public void onBeaconClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!BlockFunctions.isOneOfBlocks(BetterBeaconPlacementUtil.mineralblocks, hand)) {
			return;
		}
		
		BlockPos cpos = e.getPos();
		if (!world.getBlockState(cpos).getBlock().equals(Blocks.BEACON)) {
			return;
		}
		
		boolean set = false;
		Player player = e.getPlayer();
		while (hand.getCount() > 0) {
			BlockPos nextpos = BetterBeaconPlacementUtil.getNextLocation(world, cpos);
			if (nextpos == null) {
				if (set) {
					e.setCanceled(true);
				}
				return;
			}
			
			Block block = world.getBlockState(nextpos).getBlock();
			if (BetterBeaconPlacementConfigHandler.GENERAL.dropReplacedBlockTopBeacon.get()) {
				if (!block.equals(Blocks.AIR) && !player.isCreative()) {
					ItemEntity ei = new ItemEntity(world, cpos.getX(), cpos.getY()+2, cpos.getZ(), new ItemStack(block, 1));
					world.addFreshEntity(ei);
				}
			}
			
			if (!player.isCreative()) {
				hand.shrink(1);
			}
			
			world.setBlockAndUpdate(nextpos, Block.byItem(hand.getItem()).defaultBlockState());
			
			set = true;
			if (!player.isShiftKeyDown()) {
				break;
			}
		}
		
		e.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (!BetterBeaconPlacementConfigHandler.GENERAL.breakBeaconBaseBlocks.get()) {
			return;
		}
		
		BlockPos bpos = e.getPos();
		if (!world.getBlockState(bpos).getBlock().equals(Blocks.BEACON)) {
			return;
		}
		
		Player player = e.getPlayer();
		BetterBeaconPlacementUtil.processAllBaseBlocks(world, bpos, player.isCreative());
	}
}
