/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.17.1, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Transcending Trident ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
