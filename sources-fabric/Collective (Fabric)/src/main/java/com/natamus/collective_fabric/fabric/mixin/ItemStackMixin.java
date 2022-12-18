/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.43.
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

package com.natamus.collective_fabric.fabric.mixin;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, priority = 1001)
public abstract class ItemStackMixin {
	private static final ThreadLocal<ItemStack> COLLECTIVE$PROCESSED_STACK = new ThreadLocal<>();

	@Inject(method = "finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"))
	public void finishUsingItemA(Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
		if (livingEntity instanceof Player) {
			ItemStack itemStack = (ItemStack) (Object) this;
			COLLECTIVE$PROCESSED_STACK.set(itemStack.copy());
		}
	}

	@Inject(method = "finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "RETURN"), cancellable = true)
	public void finishUsingItemB(Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
		if (livingEntity instanceof Player) {
			InteractionHand hand = livingEntity.getUsedItemHand();

			ItemStack copyStack = COLLECTIVE$PROCESSED_STACK.get();
			ItemStack newStack = cir.getReturnValue();
			ItemStack changedStack = CollectiveItemEvents.ON_ITEM_USE_FINISHED.invoker().onItemUsedFinished((Player) livingEntity, copyStack, newStack, hand);

			if (changedStack != null) {
				cir.setReturnValue(changedStack);
			}
		}
	}
}
