/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.48.
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveEntityEvents;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "tick()V", at = @At(value = "HEAD")) 
	public void LivingEntity_tick(CallbackInfo ci) {
		Entity entity = (Entity)(Object)this;
		Level world = (Level)entity.getCommandSenderWorld();
		
		CollectiveEntityEvents.LIVING_TICK.invoker().onTick(world, entity);
	}
	
	@Inject(method = "die(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At(value = "HEAD")) 
	public void LivingEntity_die(DamageSource damageSource, CallbackInfo ci) {
		Entity entity = (Entity)(Object)this;
		Level world = (Level)entity.getCommandSenderWorld();
		
		CollectiveEntityEvents.LIVING_ENTITY_DEATH.invoker().onDeath(world, entity, damageSource);
	}
	
	@Inject(method = "calculateFallDamage(FF)I", at = @At(value = "RETURN"))
	protected int LivingEntity_calculateFallDamage(float f, float g, CallbackInfoReturnable<Boolean> ci) {
		LivingEntity livingEntity = (LivingEntity)(Object)this;
		Level world = livingEntity.getCommandSenderWorld();
		
		if (CollectiveEntityEvents.ON_FALL_DAMAGE_CALC.invoker().onFallDamageCalc(world, livingEntity, f, g) == 0) {
			return 0;
		}
		
		MobEffectInstance mobEffectInstance = livingEntity.getEffect(MobEffects.JUMP);
		float h = mobEffectInstance == null ? 0.0F : (float)(mobEffectInstance.getAmplifier() + 1);
		return Mth.ceil((f - 3.0F - h) * g);
	}
	
	@ModifyVariable(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value= "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(FF)F", ordinal = 0), ordinal = 0)
	private float LivingEntity_actuallyHurt(float f, DamageSource damageSource, float damage) {
		LivingEntity livingEntity = (LivingEntity)(Object)this;
		Level world = livingEntity.getCommandSenderWorld();

		float newDamage = CollectiveEntityEvents.ON_LIVING_DAMAGE_CALC.invoker().onLivingDamageCalc(world, livingEntity, damageSource, f);
		if (newDamage != -1 && newDamage != f) {
			return newDamage;
		}
		
		return f;
	}
	
	@Inject(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), cancellable = true)
	public boolean LivingEntity_hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> ci) {
		LivingEntity livingEntity = (LivingEntity)(Object)this;
		Level world = livingEntity.getCommandSenderWorld();

		if (!CollectiveEntityEvents.ON_LIVING_ATTACK.invoker().onLivingAttack(world, livingEntity, damageSource, f)) {
			ci.cancel();
		}

		return true;
	}
	
	@Inject(method = "dropAllDeathLoot(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At(value = "TAIL"))
	protected void dropAllDeathLoot(DamageSource damageSource, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity)(Object)this;
		Level world = livingEntity.getCommandSenderWorld();
		
		CollectiveEntityEvents.ON_ENTITY_IS_DROPPING_LOOT.invoker().onDroppingLoot(world, livingEntity, damageSource);
	}
	
	@Inject(method = "jumpFromGround", at = @At(value = "TAIL"))
	protected void LivingEntity_jumpFromGround(CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity)(Object)this;
		Level world = livingEntity.getCommandSenderWorld();
		
		CollectiveEntityEvents.ON_ENTITY_IS_JUMPING.invoker().onLivingJump(world, livingEntity);
	}
}
