/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.19.3, mod version: 3.1.
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

package com.natamus.hidehands;

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveRenderEvents;
import com.natamus.hidehands.events.HandEvent;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ClientMain implements ClientModInitializer {
	@Override
	public void onInitializeClient() { 
		registerEvents();
	}
	
	private void registerEvents() {
		CollectiveRenderEvents.RENDER_SPECIFIC_HAND.register((InteractionHand interactionHand, PoseStack poseStack, ItemStack itemStack) -> {
			return HandEvent.onHandRender(interactionHand, poseStack, itemStack);
		});
	}
}
