/*
 * This is the latest source code of GUI Followers.
 * Minecraft version: 1.19.2, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.guifollowers.events;

import java.util.ArrayList;
import java.util.List;

import com.natamus.guifollowers.config.ConfigHandler;
import com.natamus.guifollowers.util.Variables;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FollowerEvent {
	public static void onPlayerTick(Minecraft mc) {
		Player player = mc.player;
		if (player == null) {
			return;
		}
		
		if (player.tickCount % (20* ConfigHandler.timeBetweenChecksInSeconds.getValue()) != 0) {
			return;
		}
		
		Level world = player.getCommandSenderWorld();
		if (world == null) {
			return;
		}

		int dc = ConfigHandler.distanceToCheckForFollowersAround.getValue(); // distancecheck
		if (dc <= 0) {
			return;
		}
		
		Vec3 pvec = player.position();
		List<Entity> entitiesaround = world.getEntities(player, new AABB(pvec.x-dc, pvec.y-dc, pvec.z-dc, pvec.x+dc, pvec.y+dc, pvec.z+dc));
		for (Entity ea : entitiesaround) {
			if (!(ea instanceof TamableAnimal)) {
				continue;
			}
			
			TamableAnimal te = (TamableAnimal)ea;
			if (!te.isTame()) {
				continue;
			}
			
			if (!te.isOwnedBy(player)) {
				continue;
			}
			
			if (te.isInSittingPose()) {
				continue;
			}

			boolean exists = false;
			for (Entity entity : Variables.activefollowers) {
				if (entity.getUUID().equals(ea.getUUID())) {
					exists = true;
					break;
				}
			}

			if (!exists) {
				Variables.activefollowers.add(ea);
			}
		}
	}
	
	public static void onPlayerLogout(Level world, Player player) {
		Variables.activefollowers = new ArrayList<Entity>();
	}
	
	public static void onHotkeyPress() {
		Variables.activefollowers = new ArrayList<Entity>();
	}
}
