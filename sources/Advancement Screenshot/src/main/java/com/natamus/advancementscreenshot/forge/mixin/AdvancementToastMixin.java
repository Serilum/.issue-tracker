/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.19.1, mod version: 3.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Advancement Screenshot ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.advancementscreenshot.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.advancementscreenshot.util.Util;

import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;

@Mixin(value = AdvancementToast.class, priority = 1001)
public class AdvancementToastMixin {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/components/toasts/ToastComponent;J)Lnet/minecraft/client/gui/components/toasts/Toast$Visibility;", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/DisplayInfo;getFrame()Lnet/minecraft/advancements/FrameType;", ordinal = 3))
    private void render(PoseStack formattedcharsequence, ToastComponent i1, long l, CallbackInfoReturnable<Toast.Visibility> cir) {
        Util.takeScreenshot();
    }
}
