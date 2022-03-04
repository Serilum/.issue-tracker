/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.x, mod version: 4.20.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveRenderEvents;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

@Mixin(value = ItemInHandRenderer.class, priority = 1001)
public class ItemInHandRendererMixin {
	@Shadow private ItemStack mainHandItem;
	@Shadow private ItemStack offHandItem;
	
	@Inject(method = "renderHandsWithItems", at = @At(value= "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", ordinal = 0), cancellable = true)
	public void ItemInHandRenderer_renderHandsWithItems_Main(float f, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, LocalPlayer localPlayer, int i, CallbackInfo ci) {
		if (!CollectiveRenderEvents.RENDER_SPECIFIC_HAND.invoker().onRenderSpecificHand(InteractionHand.MAIN_HAND, poseStack, this.mainHandItem)) {
			bufferSource.endBatch();
			ci.cancel();
		}
	}
	
	@Inject(method = "renderHandsWithItems", at = @At(value= "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", ordinal = 1), cancellable = true)
	public void ItemInHandRenderer_renderHandsWithItems_Offhand(float f, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, LocalPlayer localPlayer, int i, CallbackInfo ci) {
		if (!CollectiveRenderEvents.RENDER_SPECIFIC_HAND.invoker().onRenderSpecificHand(InteractionHand.OFF_HAND, poseStack, this.offHandItem)) {
			bufferSource.endBatch();
			ci.cancel();		
		}
	}
}
