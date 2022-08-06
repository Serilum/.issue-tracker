/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.2, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.realisticbees.forge.mixin;

import com.natamus.realisticbees.util.Util;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Bee.class, priority = 1001)
public class BeeEntityMixin {
    private final boolean preventBeeSuffocationDamage = Util.getPreventBeeSuffocationDamage();

    @Inject(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), cancellable = true)
    private void hurt(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (preventBeeSuffocationDamage && damageSource.equals(DamageSource.IN_WALL)) {
            cir.setReturnValue(false);
        }
    }
}
