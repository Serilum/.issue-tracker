/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.dismountentity.events;

import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DismountEntityDismountEvent {
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getWorld();
		if (world.isClientSide || !e.getHand().equals(InteractionHand.MAIN_HAND)) {
			return;
		}

		Player player = e.getPlayer();
		if (!player.isShiftKeyDown()) {
			return;
		}
		
		Entity target = e.getTarget();
		List<Entity> mounted = target.getPassengers();
		if (mounted.size() > 0) {
			for (Entity entity : mounted) {
				entity.stopRiding();
			}
		}
	}
}