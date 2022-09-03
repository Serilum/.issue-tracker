/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.transcendingtrident.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.thevanillaexperience.mods.transcendingtrident.config.TranscendingTridentConfigHandler;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class ExtendedTridentItem extends TridentItem {
	public final Multimap<Attribute, AttributeModifier> defaultModifiers;
	
	public ExtendedTridentItem(Item.Properties p_i48788_1_) {
		super(p_i48788_1_);
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}
	
	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player) {
			Player playerentity = (Player)entityLiving;
			int i = this.getUseDuration(stack) - timeLeft;
			if (i >= TranscendingTridentConfigHandler.GENERAL.tridentUseDuration.get()) {
				int j = EnchantmentHelper.getRiptide(stack);
				if (j <= 0 || playerentity.isInWaterOrRain() || (PlayerFunctions.isHoldingWater(playerentity) || !TranscendingTridentConfigHandler.GENERAL.mustHoldBucketOfWater.get())) {
					if (!worldIn.isClientSide) {
						stack.hurtAndBreak(1, playerentity, (p_220047_1_) -> {
							p_220047_1_.broadcastBreakEvent(entityLiving.getUsedItemHand());
						});
						
						if (j == 0) {
							ThrownTrident tridententity = new ThrownTrident(worldIn, playerentity, stack);
							tridententity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
							if (playerentity.getAbilities().instabuild) {
								tridententity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							}
						
							worldIn.addFreshEntity(tridententity);
							worldIn.playSound((Player)null, tridententity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
							if (!playerentity.getAbilities().instabuild) {
								playerentity.getInventory().removeItem(stack);
							}
						}
					}
					
					playerentity.awardStat(Stats.ITEM_USED.get(this));
					if (j > 0) {
						float f7 = playerentity.getYRot();
						float f = playerentity.getXRot();
						float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
						float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
						float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
						float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
						float f5 = 3.0F * ((1.0F + (float)j*TranscendingTridentConfigHandler.GENERAL.tridentUsePowerModifier.get().floatValue()) / 4.0F);
						f1 = f1 * (f5 / f4);
						f2 = f2 * (f5 / f4);
						f3 = f3 * (f5 / f4);
						playerentity.push((double)f1, (double)f2, (double)f3);
						playerentity.startAutoSpinAttack(20);
						
						if (playerentity.isOnGround()) {
							float f6 = 1.1999999F;
							playerentity.move(MoverType.SELF, new Vec3(0.0D, (double)f6, 0.0D));
						}
						
						SoundEvent soundevent;
						if (j >= 3) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
						} 
						else if (j == 2) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
						} 
						else {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
						}
						
						worldIn.playSound((Player)null, playerentity, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
					}
				
				}
			}
		}
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
			if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
			return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
		} 
		else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !playerIn.isInWaterOrRain() && (!PlayerFunctions.isHoldingWater(playerIn) && TranscendingTridentConfigHandler.GENERAL.mustHoldBucketOfWater.get())) {
			return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
		} 
		else {
			playerIn.startUsingItem(handIn);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
		}
	}
}