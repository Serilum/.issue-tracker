/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.36.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.fabric.mixin;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.collective_fabric.functions.TaskFunctions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class, priority = 999)
public class PlayerMixin {
	@ModifyVariable(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value= "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(FF)F", ordinal = 0), ordinal = 0, argsOnly = true)
	private float Player_actuallyHurt(float f, DamageSource damageSource, float damage) {
		LivingEntity livingEntity = (LivingEntity)(Object)this;
		Level world = livingEntity.getCommandSenderWorld();

		float newDamage = CollectiveEntityEvents.ON_LIVING_DAMAGE_CALC.invoker().onLivingDamageCalc(world, livingEntity, damageSource, f);
		if (newDamage != -1 && newDamage != f) {
			return newDamage;
		}
		
		return f;
	}
	
	@ModifyVariable(method = "getDestroySpeed", at = @At(value= "RETURN"))
	private float Player_getDestroySpeed(float f, BlockState state) {
		Player player = (Player)(Object)this;
		Level world = player.getCommandSenderWorld();

		float newSpeed = CollectivePlayerEvents.ON_PLAYER_DIG_SPEED_CALC.invoker().onDigSpeedCalc(world, player, f, state);
		if (newSpeed != -1 && newSpeed != f) {
			return newSpeed;
		}
		
		return f;
	}
	
	@Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At(value = "HEAD"))
	private void Player_drop(ItemStack itemStack, boolean bl, CallbackInfoReturnable<ItemEntity> ci) {
		Player player = (Player)(Object)this;
		CollectiveItemEvents.ON_ITEM_TOSSED.invoker().onItemTossed(player, itemStack);
	}

	@Inject(method = "hurtCurrentlyUsedShield(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
	protected void hurtCurrentlyUsedShield(float f, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if ((player.getUseItem().getMaxDamage() - player.getUseItem().getDamageValue()) < 3) {
			InteractionHand interactionHand = player.getUsedItemHand();
			ItemStack copy = player.getUseItem().copy();
			TaskFunctions.enqueueTask(player.level, () -> {
				if (player.getUseItem().isEmpty()) {
					CollectiveItemEvents.ON_ITEM_DESTROYED.invoker().onItemDestroyed(player, copy, interactionHand);
				}
			}, 0);
		}
	}

	@ModifyVariable(method = "interactOn(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/InteractionResult;consumesAction()Z", ordinal = 0), ordinal = 1)
	public ItemStack Player_interactOn_0(ItemStack itemStack2, Entity entity, InteractionHand interactionHand) {
		Player player = (Player) (Object) this;
		ItemStack itemStack = player.getItemInHand(interactionHand);
		if (itemStack.isEmpty()) {
			if (!player.getAbilities().instabuild) {
				CollectiveItemEvents.ON_ITEM_DESTROYED.invoker().onItemDestroyed(player, itemStack2, interactionHand);
			}
		}
		return itemStack2;
	}

	@ModifyVariable(method = "interactOn(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), ordinal = 1)
	public ItemStack Player_interactOn_1(ItemStack itemStack2, Entity entity, InteractionHand interactionHand) {
		Player player = (Player) (Object) this;
		ItemStack itemStack = player.getItemInHand(interactionHand);
		if (itemStack.isEmpty()) {
			if (!player.getAbilities().instabuild) {
				CollectiveItemEvents.ON_ITEM_DESTROYED.invoker().onItemDestroyed(player, itemStack2, interactionHand);
			}
		}
		return itemStack2;
	}

	@ModifyVariable(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtEnemy(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/player/Player;)V"))
	public ItemStack Player_attack(ItemStack k, Entity entity) {
		if ((k.getMaxDamage() - k.getDamageValue()) < 3) {
			Player player = (Player) (Object) this;
			ItemStack copy = k.copy();
			TaskFunctions.enqueueTask(player.level, () -> {
				if (k.isEmpty()) {
					CollectiveItemEvents.ON_ITEM_DESTROYED.invoker().onItemDestroyed(player, copy, InteractionHand.MAIN_HAND);
				}
			}, 0);
		}
		return k;
	}
}
