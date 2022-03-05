/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.18.2, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Realistic Bees ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
