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

package com.natamus.thevanillaexperience.mods.randombonemealflowers.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.natamus.thevanillaexperience.mods.randombonemealflowers.util.RandomBoneMealFlowersUtil;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RandomBoneMealFlowersFlowerEvent {
	@SubscribeEvent
	public void onBonemeal(BonemealEvent e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		BlockPos pos = e.getPos();
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
	            	if (RandomBoneMealFlowersUtil.allflowers.contains(block) && !RandomBoneMealFlowersUtil.allflowers.contains(oldblocks.get(0))) {
	            		Block randomflower = RandomBoneMealFlowersUtil.getRandomFlower();
	            		
	            		world.setBlockAndUpdate(bp, randomflower.defaultBlockState());
	    			}

	            	oldblocks.remove(0);
	            }
	        }
	    } ).start();
	}
}
