/*
 * This is the latest source code of Move Boats.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Move Boats ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.moveboats.events;

import com.natamus.moveboats.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent.MouseButton.Post;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BoatEvent {
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
		
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		if (ConfigHandler.GENERAL.mustSneakToPickUp.get()) {
			if (!e.getEntity().isCrouching()) {
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
	public static void onMouseEvent(Post e) {
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
