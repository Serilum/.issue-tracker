/*
 * This is the latest source code of Spiders Produce Webs.
 * Minecraft version: 1.19.1, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.spidersproducewebs.events;

import java.util.List;

import com.natamus.spidersproducewebs.config.ConfigHandler;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SpiderEvent {
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (player.tickCount % ConfigHandler.GENERAL.spiderWebProduceDelayTicks.get() != 0) {
			return;
		}
		
		BlockPos ppos = player.blockPosition();
		
		int r = ConfigHandler.GENERAL.maxDistanceToSpiderBlocks.get();
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
