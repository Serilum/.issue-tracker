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

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = LocalPlayer.class, priority = 1001)
public class LocalPlayerMixin {
    @ModifyVariable(method = "aiStep", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"))
    private float LocalPlayer_f(float f) {
        return (0.3F + EnchantmentHelper.getSneakingSpeedBonus((LocalPlayer)(Object)this)) * 10.0F;
    }
}
