/*
 * This is the latest source code of Spiders Produce Webs.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.spidersproducewebs.events;

import java.util.List;

import com.natamus.spidersproducewebs.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class SpiderEvent {
	public static void onPlayerTick(ServerLevel world, ServerPlayer player) {
		if (world.isClientSide) {
			return;
		}
		
		if (player.tickCount % ConfigHandler.spiderWebProduceDelayTicks.getValue() != 0) {
			return;
		}
		
		BlockPos ppos = player.blockPosition();
		
		int r = ConfigHandler.maxDistanceToSpiderBlocks.getValue();
		List<Entity> entities = world.getEntities(player, new AABB(ppos.getX()-r, ppos.getY()-r, ppos.getZ()-r, ppos.getX()+r, ppos.getY()+r, ppos.getZ()+r));
		for (Entity entity : entities) {
			if (entity instanceof Spider || entity instanceof CaveSpider) {
				BlockPos epos = entity.blockPosition();
				if (world.getBlockState(epos).getBlock().equals(Blocks.AIR)) {
					world.setBlockAndUpdate(epos, Blocks.COBWEB.defaultBlockState());
				}
			}
		}
	}
}
