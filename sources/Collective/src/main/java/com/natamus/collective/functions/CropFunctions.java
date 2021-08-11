/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.1, mod version: 2.55.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

import java.util.Collections;
import java.util.Iterator;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.event.ForgeEventFactory;

public class CropFunctions {
	public static boolean applyBonemeal(ItemStack itemstack, Level world, BlockPos pos, Player player) {
		BlockState blockstate = world.getBlockState(pos);
		int hook = ForgeEventFactory.onApplyBonemeal(player, world, pos, blockstate, itemstack);
		if (hook != 0) return hook > 0;
		
		if (blockstate.getBlock() instanceof BonemealableBlock) {
			BonemealableBlock bonemealableblock = (BonemealableBlock)blockstate.getBlock();
			if (bonemealableblock.isValidBonemealTarget(world, pos, blockstate, world.isClientSide)) {
				if (world instanceof ServerLevel) {
					if (bonemealableblock.isBonemealSuccess(world, world.random, pos, blockstate)) {
						bonemealableblock.performBonemeal((ServerLevel)world, world.random, pos, blockstate);
					}
					
					if (!player.isCreative()) {
						itemstack.shrink(1);
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean growCrop(Level world, Player player, BlockState state, BlockPos pos) {
		if (world instanceof ServerLevel == false) {
			return false;
		}
		
		ItemStack hand = player.getItemInHand(InteractionHand.MAIN_HAND);
		Block block = state.getBlock();

		if (block instanceof BonemealableBlock) {
			BonemealableBlock igrowable = (BonemealableBlock)block;
			while (igrowable.isValidBonemealTarget(world, pos, state, world.isClientSide)) {
				if (!igrowable.isBonemealSuccess(world, world.random, pos, state)) {
					break;
				}
				igrowable.performBonemeal((ServerLevel)world, world.random, pos, state);
				state = world.getBlockState(pos);
				hand.shrink(1);
				if (hand.getCount() == 0) {
					break;
				}
			}
		}
		else {
			Iterator<Property<?>> itp = Collections.unmodifiableCollection(state.getValues().keySet()).iterator();
			
			while(itp.hasNext()) {
				Property<?> property = itp.next();
				if (property instanceof IntegerProperty) {
					IntegerProperty prop = (IntegerProperty)property;
					String name = prop.getName();
					if (name.equals("age")) {
						Comparable<?> cv = state.getValues().get(property);
						int value = Integer.parseUnsignedInt(cv.toString());
						int max = Collections.max(prop.getPossibleValues());
						if (value == max) {
							return false;
						}
	
						while (value < max) {
							world.setBlockAndUpdate(pos, world.getBlockState(pos).cycle(property));
							if (!player.isCreative()) {
								hand.shrink(1);
								if (hand.getCount() == 0) {
									break;
								}
							}
							value+=1;
							
							if (!player.isShiftKeyDown()) {
								break;
							}
						}
					}
				}
			}
		}
		
		world.levelEvent(2005, pos, 0);
		return true;
	}
	
	public static boolean growCactus(Level world, BlockPos pos) {
		int height = world.getHeight();
		for (int y = pos.getY(); y <= height; y++) {
			BlockPos uppos = new BlockPos(pos.getX(), y, pos.getZ());
			Block block = world.getBlockState(uppos).getBlock();
			if (block != Blocks.CACTUS) {
				if (block.equals(Blocks.AIR)) {
					world.setBlockAndUpdate(uppos, Blocks.CACTUS.defaultBlockState());
					world.levelEvent(2005, uppos, 0);
					world.levelEvent(2005, uppos.above(), 0);
					return true;
				}
				break;
			}
		}
		return false;
	}
	
	public static boolean growSugarcane(Level world, BlockPos pos) {
		int height = world.getHeight();
		for (int y = pos.getY(); y <= height; y++) {
			BlockPos uppos = new BlockPos(pos.getX(), y, pos.getZ());
			Block block = world.getBlockState(uppos).getBlock();
			if (block != Blocks.SUGAR_CANE) {
				if (block.equals(Blocks.AIR)) {
					world.setBlockAndUpdate(uppos, Blocks.SUGAR_CANE.defaultBlockState());
					world.levelEvent(2005, uppos, 0);
					world.levelEvent(2005, uppos.above(), 0);
					return true;
				}
				break;
			}
		}
		return false;
	}
	
	public static boolean growVine(Level world, BlockPos pos) {
		for (int y = pos.getY(); y > 0; y--) {
			BlockPos downpos = new BlockPos(pos.getX(), y, pos.getZ());
			Block block = world.getBlockState(downpos).getBlock();
			if (block != Blocks.VINE) {
				if (block.equals(Blocks.AIR)) {
					world.setBlockAndUpdate(downpos, world.getBlockState(pos));
					world.levelEvent(2005, downpos, 0);
					world.levelEvent(2005, downpos.below(), 0);
					return true;
				}
				break;
			}
		}
		return false;
	}
}
