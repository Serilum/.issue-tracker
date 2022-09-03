/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.19.2, mod version: 3.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
