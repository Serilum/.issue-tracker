/*
 * This is the latest source code of Spiders Produce Webs.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Spiders Produce Webs ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.spidersproducewebs.events;

import java.util.List;

import com.natamus.spidersproducewebs.config.ConfigHandler;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SpiderEvent {
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		PlayerEntity player = e.player;
		World world = player.getEntityWorld();
		if (world.isRemote || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (player.ticksExisted % ConfigHandler.GENERAL.spiderWebProduceDelayTicks.get() != 0) {
			return;
		}
		
		BlockPos ppos = player.getPosition();
		
		int r = ConfigHandler.GENERAL.maxDistanceToSpiderBlocks.get();
		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(ppos.getX()-r, ppos.getY()-r, ppos.getZ()-r, ppos.getX()+r, ppos.getY()+r, ppos.getZ()+r));
		for (Entity entity : entities) {
			if (entity instanceof SpiderEntity || entity instanceof CaveSpiderEntity) {
				BlockPos epos = entity.getPosition();
				if (world.getBlockState(epos).getBlock().equals(Blocks.AIR)) {
					world.setBlockState(epos, Blocks.COBWEB.getDefaultState());
				}
			}
		}
	}
}
