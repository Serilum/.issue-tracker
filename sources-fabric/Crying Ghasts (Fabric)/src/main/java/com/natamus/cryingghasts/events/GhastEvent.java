/*
 * This is the latest source code of Crying Ghasts.
 * Minecraft version: 1.19.3, mod version: 2.1.
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

package com.natamus.cryingghasts.events;

import java.util.List;

import com.natamus.cryingghasts.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GhastEvent {
	public static void onPlayerTick(ServerLevel world, ServerPlayer player) {
		if (player.tickCount % ConfigHandler.ghastTearDelayTicks != 0) {
			return;
		}
		
		BlockPos ppos = player.blockPosition();
		ItemStack tear = new ItemStack(Items.GHAST_TEAR, 1);
		
		int r = ConfigHandler.maxDistanceToGhastBlocks;
		List<Entity> entities = world.getEntities(player, new AABB(ppos.getX()-r, ppos.getY()-r, ppos.getZ()-r, ppos.getX()+r, ppos.getY()+r, ppos.getZ()+r));
		for (Entity entity : entities) {
			if (entity instanceof Ghast) {
				Vec3 gpos = entity.position();
				world.addFreshEntity(new ItemEntity(world, gpos.x, gpos.y+2, gpos.z, tear));
			}
		}
	}
}
