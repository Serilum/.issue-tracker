/*
 * This is the latest source code of Infinite Trading.
 * Minecraft version: 1.19.2, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.infinitetrading.fabric.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

@Mixin(MerchantOffer.class)
public class MerchantOfferMixin {
	@Shadow private int uses;
	@Shadow private @Final @Mutable int maxUses;
	@Shadow private int demand;
	
	@Inject(method = " <init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
	public void MerchantOffer(CompoundTag compoundTag, CallbackInfo ci) {
		uses = 0;
		maxUses = Integer.MAX_VALUE;
		demand = 0;
	}
	
	@Inject(method = " <init>(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;IIIFI)V", at = @At("TAIL"))
	public void MerchantOffer(ItemStack itemStack, ItemStack itemStack2, ItemStack itemStack3, int i, int j, int k, float f, int l, CallbackInfo ci) {
		uses = 0;
		maxUses = Integer.MAX_VALUE;
		demand = 0;
	}	
}
