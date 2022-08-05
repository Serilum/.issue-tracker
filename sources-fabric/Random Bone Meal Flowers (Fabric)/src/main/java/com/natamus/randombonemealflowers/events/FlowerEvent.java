/*
 * This is the latest source code of Random Bone Meal Flowers.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.randombonemealflowers.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.natamus.randombonemealflowers.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FlowerEvent {
	public static void onBonemeal(Level world, BlockPos pos, BlockState state, ItemStack stack) {
		if (world.isClientSide) {
			return;
		}
		
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		
		List<Block> oldblocks = new ArrayList<Block>();
		Iterator<BlockPos> it = BlockPos.betweenClosedStream(x-4, y, z-4, x+4, y+1, z+4).iterator();
		while (it.hasNext()) {
			BlockPos bp = it.next();
			Block block = world.getBlockState(bp).getBlock();
			oldblocks.add(block);
		}
		
	    new Thread( new Runnable() {
	        public void run()  {
	            try  { Thread.sleep( 0 ); }
	            catch (InterruptedException ie)  {}
	            Iterator<BlockPos> newit = BlockPos.betweenClosedStream(x-4, y, z-4, x+4, y+1, z+4).iterator();
	            while (newit.hasNext()) {
	            	BlockPos bp = newit.next();
	            	Block block = world.getBlockState(bp).getBlock();
	            	if (Util.allflowers.contains(block) && !Util.allflowers.contains(oldblocks.get(0))) {
	            		Block randomflower = Util.getRandomFlower();
	            		
	            		world.setBlockAndUpdate(bp, randomflower.defaultBlockState());
	    			}

	            	oldblocks.remove(0);
	            }
	        }
	    } ).start();
	}
}
