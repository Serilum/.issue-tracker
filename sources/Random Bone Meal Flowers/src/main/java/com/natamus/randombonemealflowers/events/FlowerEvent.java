/*
 * This is the latest source code of Random Bone Meal Flowers.
 * Minecraft version: 1.19.2, mod version: 3.2.
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

package com.natamus.randombonemealflowers.events;

import com.natamus.randombonemealflowers.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber
public class FlowerEvent {
	@SubscribeEvent
	public void onBonemeal(BonemealEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}

		BlockPos pos = e.getPos();
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		
		List<Block> oldblocks = new ArrayList<Block>();
		Iterator<BlockPos> it = BlockPos.betweenClosedStream(x-6, y, z-6, x+6, y+1, z+6).iterator();
		while (it.hasNext()) {
			BlockPos bp = it.next();
			Block block = world.getBlockState(bp).getBlock();
			oldblocks.add(block);
		}

	   world.getServer().tell(new TickTask(world.getServer().getTickCount(), () -> {
		   Iterator < BlockPos > newit = BlockPos.betweenClosedStream(x - 6, y, z - 6, x + 6, y + 1, z + 6).iterator();
		   while (newit.hasNext()) {
			   BlockPos bp = newit.next();
			   Block block = world.getBlockState(bp).getBlock();
			   if (Util.allflowers.contains(block) && !Util.allflowers.contains(oldblocks.get(0))) {
				   Block randomflower = Util.getRandomFlower();

				   world.setBlockAndUpdate(bp, randomflower.defaultBlockState());
			   }

			   oldblocks.remove(0);
		   }
	   }));
	}
}
