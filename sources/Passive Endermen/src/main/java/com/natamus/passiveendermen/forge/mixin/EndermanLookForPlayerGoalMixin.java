/*
 * This is the latest source code of Passive Endermen.
 * Minecraft version: 1.19.1, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.passiveendermen.forge.mixin;

import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.passiveendermen.util.Reference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/entity/monster/EnderMan$EndermanLookForPlayerGoal", priority = 1001)
public class EndermanLookForPlayerGoalMixin {
    private final boolean preventEndermenFromAttacking = ConfigFunctions.getDictValues(Reference.MOD_ID).get("preventEndermenFromAttackingFirst").equals("true");

    @Inject(method = "canUse()Z", at = @At(value = "HEAD"), cancellable = true)
    public void canUse(CallbackInfoReturnable<Boolean> cir) {
        if (preventEndermenFromAttacking) {
            cir.setReturnValue(false);
        }
    }
}
