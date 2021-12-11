/*
 * This is the latest source code of Move Minecarts.
 * Minecraft version: 1.18.1, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Move Minecarts ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.moveminecarts.events;

import com.natamus.moveminecarts.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MinecartEvent {
	private static Entity pickedupminecart = null;
	private static boolean rmbdown = false;
	
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent e) {
		if (pickedupminecart == null) {
			return;
		}
		
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || e.phase != Phase.START) {
			return;
		}
		
		for (Entity passenger : pickedupminecart.getPassengers()) {
			if (passenger.is(player)) {
				pickedupminecart = null;
				return;
			}
		}
		
		Vec3 look = player.getLookAngle();
		float distance = 2.0F;
		double dx = player.getX() + (look.x * distance);
		double dy = player.getY() + player.getEyeHeight();
		double dz = player.getZ() + (look.z * distance);
		pickedupminecart.setPos(dx, dy, dz);
	}
	
	@SubscribeEvent
	public static void onMinecartClick(PlayerInteractEvent.EntityInteract e) {
		if (!e.getTarget().getClass().getName().toLowerCase().contains(".minecart")) {
			return;
		}
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		if (ConfigHandler.GENERAL.mustSneakToPickUp.get()) {
			if (!e.getPlayer().isCrouching()) {
				return;
			}
		}
		
		if (rmbdown) {
			e.setCanceled(true);
			pickedupminecart = (Entity)e.getTarget();
		}
		else if (pickedupminecart != null) {
			e.setCanceled(true);
			pickedupminecart = null;
		}
	}
	
	@SubscribeEvent
	public static void onMouseEvent(MouseInputEvent e) {
		if (e.getButton() != 1) return;
		if (!rmbdown) {
			if (pickedupminecart != null) {
				pickedupminecart = null;
			}
			rmbdown = true;
		} else {
			rmbdown = false;
		}
	}
}
