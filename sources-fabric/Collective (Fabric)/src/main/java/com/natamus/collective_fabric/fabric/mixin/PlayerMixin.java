/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.51.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = Player.class, priority = 1001)
public class PlayerMixin {
	@ModifyVariable(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value= "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(FF)F", ordinal = 0), ordinal = 0)
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
}
