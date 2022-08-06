/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.19.2, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.transcendingtrident.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.natamus.transcendingtrident.config.ConfigHandler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;

@Mixin(value = TridentItem.class, priority = 1001)
public class TridentItemMixin {
    @ModifyVariable(method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Mth;sqrt(F)F"), ordinal = 2)
    private int TridentItem_releaseUsing(int j, ItemStack p_43394_, Level p_43395_, LivingEntity p_43396_, int p_43397_) {
    	return Math.round(j*ConfigHandler.GENERAL.tridentUsePowerModifier.get().floatValue());
    }
}
