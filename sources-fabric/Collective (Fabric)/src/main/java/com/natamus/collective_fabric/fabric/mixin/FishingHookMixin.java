/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.44.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.fabric.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;

import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;

@Mixin(value = FishingHook.class, priority = 1001)
public abstract class FishingHookMixin {
	@ModifyVariable(method = "retrieve", at = @At(value= "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"))
	private List<ItemStack> FishingHook_retrieve(List<ItemStack> list, ItemStack itemStack) {
		FishingHook hook = (FishingHook)(Object)this;
		CollectiveItemEvents.ON_ITEM_FISHED.invoker().onItemFished(list, hook);
		
		return list;
	}
}
