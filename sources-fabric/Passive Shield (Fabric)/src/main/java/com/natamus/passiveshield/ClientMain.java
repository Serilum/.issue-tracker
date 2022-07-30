/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.19.1, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Passive Shield ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.passiveshield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveRenderEvents;
import com.natamus.passiveshield.events.ClientEvent;
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
            return ClientEvent.onHandRender(interactionHand, poseStack, itemStack);
        });
    }
}
