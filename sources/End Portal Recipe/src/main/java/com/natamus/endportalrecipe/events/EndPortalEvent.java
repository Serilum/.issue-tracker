/*
 * This is the latest source code of End Portal Recipe.
 * Minecraft version: 1.16.5, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of End Portal Recipe ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.endportalrecipe.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.primitives.Ints;
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.endportalrecipe.config.ConfigHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EndPortalEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof EnderDragonEntity == false) {
			return;
		}
		
		Entity source = e.getSource().getEntity();
		if (entity instanceof PlayerEntity == false) {
			return;
		}

		PlayerEntity player = (PlayerEntity)source;
		ItemStack egg = new ItemStack(Blocks.DRAGON_EGG, 1);
		ItemFunctions.giveOrDropItemStack(player, egg);
	}
	
	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack itemstack = e.getItemStack();
		if (itemstack.getItem().equals(Items.FLINT_AND_STEEL)) {
			int aircount = 0;
			BlockPos centerpos = null;
			
			BlockPos cpos = e.getPos();
			Iterator<BlockPos> it = BlockPos.betweenClosedStream(cpos.getX()-1, cpos.getY()+1, cpos.getZ()-1, cpos.getX()+1, cpos.getY()+1, cpos.getZ()+1).iterator();
			while (it.hasNext()) {
				aircount = 0;
				BlockPos np = it.next();
				Iterator<BlockPos> npit = BlockPos.betweenClosedStream(np.getX()-1, np.getY(), np.getZ()-1, np.getX()+1, np.getY(), np.getZ()+1).iterator();
				while (npit.hasNext()) {
					BlockPos npp = npit.next();
					Block block = world.getBlockState(npp).getBlock();
					if (block.equals(Blocks.AIR) || block.equals(Blocks.FIRE)) {
						if (aircount == 4) {
							centerpos = npp.immutable();
						}
						aircount++;
					}
				}
				if (aircount == 9) {
					break;
				}
				aircount++;
			}
			
			if (aircount != 9) {
				return;
			}
			
			if (centerpos != null) {
				//       1 2 3 
				//    5         9
				//   10         14
				//   15         19
				//     21 22 23
				List<Integer> portalnumbers = new ArrayList<Integer>(Ints.asList(1, 2, 3, 5, 9, 10, 14, 15, 19, 21, 22, 23));
				
				int pi = 0;
				Iterator<BlockPos> centerit = BlockPos.betweenClosedStream(centerpos.getX()-2, centerpos.getY(), centerpos.getZ()-2, centerpos.getX()+2, centerpos.getY(), centerpos.getZ()+2).iterator();
				while (centerit.hasNext()) {
					BlockPos ncp = centerit.next();
					if (portalnumbers.contains(pi)) {
						BlockState ibs = world.getBlockState(ncp);
						if (ibs.getBlock().equals(Blocks.END_PORTAL_FRAME)) {
							if (!BlockFunctions.isFilledPortalFrame(ibs)) {
								break;
							}
						}
						else {
							break;
						}
					}
					pi++;
				}
				
				if (pi == 25) {
					Iterator<BlockPos> portalposses = BlockPos.betweenClosedStream(centerpos.getX()-1, centerpos.getY(), centerpos.getZ()-1, centerpos.getX()+1, centerpos.getY(), centerpos.getZ()+1).iterator();
					while (portalposses.hasNext()) {
						BlockPos ppp = portalposses.next();
						world.setBlockAndUpdate(ppp, Blocks.END_PORTAL.defaultBlockState());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onLeftClick(PlayerInteractEvent.LeftClickBlock e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (hand.getToolTypes().contains(ToolType.PICKAXE)) {
			if (ConfigHandler.GENERAL.mustHaveSilkTouchToBreakPortal.get()) {
				if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, hand) < 1) {
					return;
				}
			}
			
			BlockPos cpos = e.getPos().immutable();
			BlockState cbs = world.getBlockState(cpos);
			if (cbs.getBlock().equals(Blocks.END_PORTAL_FRAME)) {
				PlayerEntity player = e.getPlayer();
				ItemStack portalframe = new ItemStack(Blocks.END_PORTAL_FRAME, 1);
				ItemStack endereye = new ItemStack(Items.ENDER_EYE, 1);
				
				if (ConfigHandler.GENERAL.addBrokenPortalFramesToInventory.get()) {
					ItemFunctions.giveOrDropItemStack(player, portalframe);
					if (BlockFunctions.isFilledPortalFrame(cbs)) {
						ItemFunctions.giveOrDropItemStack(player, endereye);
					}
				}
				else {
					ItemEntity frame = new ItemEntity(world, cpos.getX(), cpos.getY()+1, cpos.getZ(), portalframe);
					world.addFreshEntity(frame);
					
					if (BlockFunctions.isFilledPortalFrame(cbs)) {
						ItemEntity eoe = new ItemEntity(world, cpos.getX(), cpos.getY()+1, cpos.getZ(), endereye);
						world.addFreshEntity(eoe);
					}
				}
				
				world.destroyBlock(cpos, false);
				Iterator<BlockPos> it = BlockPos.betweenClosedStream(cpos.getX()-3,  cpos.getY(), cpos.getZ()-3, cpos.getX()+3, cpos.getY(), cpos.getZ()+3).iterator();
				while (it.hasNext()) {
					BlockPos np = it.next();
					if (world.getBlockState(np).getBlock().equals(Blocks.END_PORTAL)) {
						world.setBlockAndUpdate(np, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}
	}
}
