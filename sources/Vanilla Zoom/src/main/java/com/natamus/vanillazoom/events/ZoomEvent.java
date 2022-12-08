/*
 * This is the latest source code of Vanilla Zoom.
 * Minecraft version: 1.19.3, mod version: 1.0.
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

package com.natamus.vanillazoom.events;

import com.natamus.vanillazoom.util.Variables;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpyglassItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ZoomEvent {
    private static Minecraft mc = Minecraft.getInstance();
	private static KeyMapping useKeyMapping = mc.options.keyUse;

	public static boolean showZoom = false;
	private static ItemStack previousStack = null;

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent e) {
		if (!e.phase.equals(TickEvent.Phase.START)) {
			return;
		}

		Player player = mc.player;
		if (player == null) {
			return;
		}

		if (!player.level.isClientSide) {
			return;
		}

		boolean forceRelease = false;
		if (showZoom) {
			KeyMapping.set(useKeyMapping.getKey(), true);
			KeyMapping.click(useKeyMapping.getKey());

			if (!(player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof SpyglassItem)) {
				forceRelease = true;
			}
		}

		if (Variables.hotkey.isDown() && !forceRelease) {
			if (!showZoom) {
				showZoom = true;

				previousStack = player.getItemInHand(InteractionHand.OFF_HAND).copy();

				ItemStack spyglassStack = new ItemStack(Items.SPYGLASS);
				spyglassStack.setHoverName(Component.literal(""));
				player.setItemInHand(InteractionHand.OFF_HAND, spyglassStack);
			}
		}
		else {
			if (showZoom) {
				showZoom = false;
				KeyMapping.set(useKeyMapping.getKey(), false);

				if (previousStack != null) {
					player.setItemInHand(InteractionHand.OFF_HAND, previousStack.copy());
					previousStack = null;
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemUse(PlayerInteractEvent.RightClickItem e) {
		if (showZoom) {
			if (!(e.getItemStack().getItem() instanceof SpyglassItem)) {
				e.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
		if (showZoom) {
			e.setCanceled(true);
		}
	}
}
