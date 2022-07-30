/*
 * This is the latest source code of End Portal Recipe.
 * Minecraft version: 1.19.1, mod version: 2.9.
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

import com.google.common.primitives.Ints;
import com.natamus.collective_fabric.functions.BlockFunctions;
import com.natamus.collective_fabric.functions.ItemFunctions;
import com.natamus.collective_fabric.functions.ToolFunctions;
import com.natamus.endportalrecipe.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EndPortalEvent {
	public static void mobItemDrop(Level world, Entity entity, DamageSource damageSource) {
		if (world.isClientSide) {
			return;
		}
		
		if (!(entity instanceof EnderDragon)) {
			return;
		}
		
		Entity source = damageSource.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}

		Player player = (Player)source;
		ItemStack egg = new ItemStack(Blocks.DRAGON_EGG, 1);
		ItemFunctions.giveOrDropItemStack(player, egg);
	}
	
	public static boolean onRightClick(Level world, Player player, InteractionHand hand, BlockPos cpos, BlockHitResult hitVec) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (world.isClientSide) {
			return true;
		}

		if (itemstack.getItem().equals(Items.FLINT_AND_STEEL)) {
			int aircount = 0;
			BlockPos centerpos = null;

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
				return true;
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

		return true;
	}
	
	public static boolean onLeftClick(Level world, Player player, BlockPos cpos, Direction direction) {
		if (world.isClientSide) {
			return true;
		}
		
		ItemStack hand = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (ToolFunctions.isPickaxe(hand)) {
			if (ConfigHandler.mustHaveSilkTouchToBreakPortal.getValue()) {
				if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, hand) < 1) {
					return true;
				}
			}

			BlockState cbs = world.getBlockState(cpos);
			if (cbs.getBlock().equals(Blocks.END_PORTAL_FRAME)) {
				ItemStack portalframe = new ItemStack(Blocks.END_PORTAL_FRAME, 1);
				ItemStack endereye = new ItemStack(Items.ENDER_EYE, 1);
				
				if (ConfigHandler.addBrokenPortalFramesToInventory.getValue()) {
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

				return false;
			}
		}

		return true;
	}
}
