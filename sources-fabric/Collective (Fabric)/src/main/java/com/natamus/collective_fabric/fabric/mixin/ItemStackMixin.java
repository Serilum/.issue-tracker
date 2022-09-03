/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.50.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.collective_fabric.fabric.mixin;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;
import com.natamus.collective_fabric.functions.TaskFunctions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, priority = 999)
public abstract class ItemStackMixin {
	@Inject(method = "finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"), cancellable = true)
	public void finishUsingItem(Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
		if (livingEntity instanceof Player) {
			ItemStack itemStack = (ItemStack) (Object) this;
			ItemStack copyStack = itemStack.copy();
			ItemStack newStack = itemStack.getItem().finishUsingItem(itemStack, level, livingEntity);

			InteractionHand hand = livingEntity.getUsedItemHand();
			CollectiveItemEvents.ON_ITEM_USE_FINISHED.invoker().onItemUsedFinished((Player) livingEntity, copyStack, newStack, hand);

			cir.setReturnValue(newStack);
		}
	}
}
