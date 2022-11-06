/*
 * This is the latest source code of Faster Crouching.
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

package com.natamus.fastercrouching.fabric.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, priority = 1001)
public class LivingEntityMixin {
    @Shadow
    private float speed;

    @Inject(method = "setSpeed", at = @At(value = "HEAD"), cancellable = true)
    public void setSpeed(float pSpeed, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        if (livingEntity instanceof Player) {
            if (livingEntity.isCrouching()) {
                this.speed = pSpeed * 10.0F;
                ci.cancel();
            }
        }
    }
}
