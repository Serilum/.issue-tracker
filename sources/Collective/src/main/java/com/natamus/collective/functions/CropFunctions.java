/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.16.5, mod version: 2.27.
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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CropFunctions {
	public static boolean growCrop(World world, PlayerEntity player, BlockState state, BlockPos pos) {
		if (world instanceof ServerWorld == false) {
			return false;
		}
		
		ItemStack hand = player.getItemInHand(Hand.MAIN_HAND);
		Block block = state.getBlock();

		if (block instanceof IGrowable) {
			IGrowable igrowable = (IGrowable)block;
			while (igrowable.isValidBonemealTarget(world, pos, state, world.isClientSide)) {
				if (!igrowable.isBonemealSuccess(world, world.random, pos, state)) {
					break;
				}
				igrowable.performBonemeal((ServerWorld)world, world.random, pos, state);
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
	
	public static boolean growCactus(World world, BlockPos pos) {
		for (int y = pos.getY(); y <= 256; y++) {
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
	
	public static boolean growSugarcane(World world, BlockPos pos) {
		for (int y = pos.getY(); y <= 256; y++) {
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
	
	public static boolean growVine(World world, BlockPos pos) {
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
