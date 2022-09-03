/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.guifollowers.events;

import java.util.ArrayList;
import java.util.List;

import com.natamus.thevanillaexperience.mods.guifollowers.config.GUIFollowersConfigHandler;
import com.natamus.thevanillaexperience.mods.guifollowers.util.GUIFollowersVariables;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GUIFollowersFollowerEvent {
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		if (!e.phase.equals(Phase.START)) {
			return;
		}
		
		Player player = e.player;
		if (player == null) {
			return;
		}
		
		if (player.tickCount % (20* GUIFollowersConfigHandler.GENERAL.timeBetweenChecksInSeconds.get()) != 0) {
			return;
		}
		
		Level world = player.getCommandSenderWorld();
		if (world == null) {
			return;
		}

		int dc = GUIFollowersConfigHandler.GENERAL.distanceToCheckForFollowersAround.get(); // distancecheck
		if (dc <= 0) {
			return;
		}
		
		Vec3 pvec = player.position();
		List<Entity> entitiesaround = world.getEntities(player, new AABB(pvec.x-dc, pvec.y-dc, pvec.z-dc, pvec.x+dc, pvec.y+dc, pvec.z+dc));
		for (Entity ea : entitiesaround) {
			if (ea instanceof TamableAnimal == false) {
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
			
			if (!GUIFollowersVariables.activefollowers.contains(ea)) {
				GUIFollowersVariables.activefollowers.add(ea);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent e) {
		GUIFollowersVariables.activefollowers = new ArrayList<Entity>();
	}
	
	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent e) {
		if (e.getAction() != 1) {
			return;
		}

		if (e.getKey() == GUIFollowersVariables.clearlist_hotkey.getKey().getValue()) {
			GUIFollowersVariables.activefollowers = new ArrayList<Entity>();
		}
	}
}
