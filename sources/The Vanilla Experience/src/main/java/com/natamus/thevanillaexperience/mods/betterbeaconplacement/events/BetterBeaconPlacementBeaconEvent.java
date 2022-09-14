/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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
