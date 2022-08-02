/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.1, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.realisticbees.forge.mixin;

import com.natamus.realisticbees.util.Util;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BeehiveBlockEntity.class, priority = 1001)
public class BeehiveBlockEntityMixin {
    private final int beehiveBeeSpace = Util.getBeehiveBeeSpace();

    @Inject(method = "isFull()Z", at = @At(value = "HEAD"), cancellable = true)
    private void hurt(CallbackInfoReturnable<Boolean> cir) {
        BeehiveBlockEntity beehive = (BeehiveBlockEntity)(Object)this;
        cir.setReturnValue(beehive.getOccupantCount() == beehiveBeeSpace);
    }

    @ModifyConstant(method = "addOccupantWithPresetTicks(Lnet/minecraft/world/entity/Entity;ZI)V", constant = @Constant(intValue = 3))
    public int addOccupantWithPresetTicks_increaseSize(int size) {
        return beehiveBeeSpace;
    }
}
