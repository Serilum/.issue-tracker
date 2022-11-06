/*
 * This is the latest source code of Vanilla Zoom.
 * Minecraft version: 1.19.2, mod version: 1.1.
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
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ZoomEvent {
    private static Minecraft mc = Minecraft.getInstance();
	private static KeyMapping useKeyMapping = mc.options.keyUse;

	public static boolean showZoom = false;
	private static ItemStack previousStack = null;

	public static void onClientTick() {
		Player player = mc.player;
		if (player == null) {
			return;
		}

		if (!player.level.isClientSide) {
			return;
		}

		boolean forceRelease = false;
		if (showZoom) {
			KeyMapping.set(useKeyMapping.getDefaultKey(), true);
			KeyMapping.click(useKeyMapping.getDefaultKey());

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
				KeyMapping.set(useKeyMapping.getDefaultKey(), false);

				if (previousStack != null) {
					player.setItemInHand(InteractionHand.OFF_HAND, previousStack.copy());
					previousStack = null;
				}
			}
		}
	}

	public static InteractionResultHolder<ItemStack> onItemUse(Player player, Level level, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (showZoom) {
			if (!(itemStack.getItem() instanceof SpyglassItem)) {
				return InteractionResultHolder.fail(itemStack);
			}
		}
		return InteractionResultHolder.pass(itemStack);
	}

	public static InteractionResult onEntityInteract(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
		if (showZoom) {
			return InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}
}
