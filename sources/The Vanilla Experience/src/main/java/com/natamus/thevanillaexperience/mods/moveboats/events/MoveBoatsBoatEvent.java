/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.moveboats.events;

import com.natamus.thevanillaexperience.mods.moveboats.config.MoveBoatsConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MoveBoatsBoatEvent {
	private static Boat pickedupboat = null;
	private static boolean rmbdown = false;
	
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent e) {
		if (pickedupboat == null) {
			return;
		}
		
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || e.phase != Phase.START) {
			return;
		}
		
		for (Entity passenger : pickedupboat.getPassengers()) {
			if (passenger.is(player)) {
				pickedupboat = null;
				return;
			}
		}
		
		Vec3 look = player.getLookAngle();
		float distance = 2.0F;
		double dx = player.getX() + (look.x * distance);
		double dy = player.getY() + player.getEyeHeight();
		double dz = player.getZ() + (look.z * distance);
		pickedupboat.setPos(dx, dy, dz);
		pickedupboat.fallDistance = 0.0F;
	}
	
	@SubscribeEvent
	public static void onBoatClick(PlayerInteractEvent.EntityInteract e) {
		if (e.getTarget() instanceof Boat == false) {
			return;
		}
		
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (MoveBoatsConfigHandler.GENERAL.mustSneakToPickUp.get()) {
			if (!e.getPlayer().isCrouching()) {
				return;
			}
		}
		
		if (rmbdown) {
			e.setCanceled(true);
			pickedupboat = (Boat)e.getTarget();
		}
		else if (pickedupboat != null) {
			e.setCanceled(true);
			pickedupboat = null;
		}
	}
	
	@SubscribeEvent
	public static void onMouseEvent(MouseInputEvent e) {
		if (e.getButton() != 1) return;
		if (!rmbdown) {
			if (pickedupboat != null) {
				pickedupboat = null;
			}
			rmbdown = true;
		} else {
			rmbdown = false;
		}
	}
}
