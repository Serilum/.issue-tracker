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

package com.natamus.thevanillaexperience.mods.betterconduitplacement.events;

import com.natamus.thevanillaexperience.mods.betterconduitplacement.config.BetterConduitPlacementConfigHandler;
import com.natamus.thevanillaexperience.mods.betterconduitplacement.util.BetterConduitPlacementUtil;
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BetterConduitPlacementConduitEvent {
	@SubscribeEvent
	public void onWaterClick(PlayerInteractEvent.RightClickItem e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (!itemstack.getItem().equals(Items.CONDUIT)) {
			return;
		}
		
		Player player = e.getPlayer();
		Vec3 look = player.getLookAngle();
		float distance = 2.0F;
		double dx = player.getX() + (look.x * distance);
		double dy = player.getY() + (look.y * distance) + 2;
		double dz = player.getZ() + (look.z * distance);
		
		BlockPos frontpos = new BlockPos(dx, dy, dz);
		
		if (!world.getBlockState(frontpos).getBlock().equals(Blocks.WATER)) {
			return;
		}
		
		if (!player.isCreative()) {
			itemstack.shrink(1);
		}
		
		world.setBlockAndUpdate(frontpos, Blocks.CONDUIT.defaultBlockState());
	}
	
	@SubscribeEvent
	public void onConduitClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		BlockPos cpos = e.getPos();
		if (!world.getBlockState(cpos).getBlock().equals(Blocks.CONDUIT)) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!BlockFunctions.isOneOfBlocks(BetterConduitPlacementUtil.conduitblocks, hand)) {
			return;
		}
		
		boolean set = false;
		Player player = e.getPlayer();
		while (hand.getCount() > 0) {
			BlockPos nextpos = BetterConduitPlacementUtil.getNextLocation(world, cpos);
			if (nextpos == null) {
				break;
			}
			
			Block block = world.getBlockState(nextpos).getBlock();
			if (BetterConduitPlacementConfigHandler.GENERAL.dropReplacedBlockTopConduit.get()) {
				if (!block.equals(Blocks.AIR) && !block.equals(Blocks.WATER)) {
					ItemEntity ei = new ItemEntity(world, cpos.getX(), cpos.getY()+1, cpos.getZ(), new ItemStack(block, 1));
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
		
		if (set) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (!BetterConduitPlacementConfigHandler.GENERAL.breakConduitBlocks.get()) {
			return;
		}
		
		BlockPos bpos = e.getPos();
		if (!world.getBlockState(bpos).getBlock().equals(Blocks.CONDUIT)) {
			return;
		}
		
		BetterConduitPlacementUtil.destroyAllConduitBlocks(world, bpos);
	}
}
