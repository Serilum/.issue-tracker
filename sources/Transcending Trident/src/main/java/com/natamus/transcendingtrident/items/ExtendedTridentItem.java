/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.16.5, mod version: 1.5.
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

package com.natamus.transcendingtrident.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.transcendingtrident.config.ConfigHandler;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ExtendedTridentItem extends TridentItem {
	public final Multimap<Attribute, AttributeModifier> field_234812_a_;
	
	public ExtendedTridentItem(Item.Properties p_i48788_1_) {
		super(p_i48788_1_);
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
		this.field_234812_a_ = builder.build();
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity playerentity = (PlayerEntity)entityLiving;
			int i = this.getUseDuration(stack) - timeLeft;
			if (i >= ConfigHandler.GENERAL.tridentUseDuration.get()) {
				int j = EnchantmentHelper.getRiptideModifier(stack);
				if (j <= 0 || playerentity.isWet() || (PlayerFunctions.isHoldingWater(playerentity) || !ConfigHandler.GENERAL.mustHoldBucketOfWater.get())) {
					if (!worldIn.isRemote) {
						stack.damageItem(1, playerentity, (p_220047_1_) -> {
							p_220047_1_.sendBreakAnimation(entityLiving.getActiveHand());
						});
						
						if (j == 0) {
							TridentEntity tridententity = new TridentEntity(worldIn, playerentity, stack);
							tridententity.func_234612_a_(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
							if (playerentity.abilities.isCreativeMode) {
								tridententity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
							}
						
							worldIn.addEntity(tridententity);
							worldIn.playMovingSound((PlayerEntity)null, tridententity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
							if (!playerentity.abilities.isCreativeMode) {
								playerentity.inventory.deleteStack(stack);
							}
						}
					}
					
					playerentity.addStat(Stats.ITEM_USED.get(this));
					if (j > 0) {
						float f7 = playerentity.rotationYaw;
						float f = playerentity.rotationPitch;
						float f1 = -MathHelper.sin(f7 * ((float)Math.PI / 180F)) * MathHelper.cos(f * ((float)Math.PI / 180F));
						float f2 = -MathHelper.sin(f * ((float)Math.PI / 180F));
						float f3 = MathHelper.cos(f7 * ((float)Math.PI / 180F)) * MathHelper.cos(f * ((float)Math.PI / 180F));
						float f4 = MathHelper.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
						float f5 = 3.0F * ((1.0F + (float)j*ConfigHandler.GENERAL.tridentUsePowerModifier.get().floatValue()) / 4.0F);
						f1 = f1 * (f5 / f4);
						f2 = f2 * (f5 / f4);
						f3 = f3 * (f5 / f4);
						playerentity.addVelocity((double)f1, (double)f2, (double)f3);
						playerentity.startSpinAttack(20);
						
						if (playerentity.isOnGround()) {
							float f6 = 1.1999999F;
							playerentity.move(MoverType.SELF, new Vector3d(0.0D, (double)f6, 0.0D));
						}
						
						SoundEvent soundevent;
						if (j >= 3) {
							soundevent = SoundEvents.ITEM_TRIDENT_RIPTIDE_3;
						} 
						else if (j == 2) {
							soundevent = SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
						} 
						else {
							soundevent = SoundEvents.ITEM_TRIDENT_RIPTIDE_1;
						}
						
						worldIn.playMovingSound((PlayerEntity)null, playerentity, soundevent, SoundCategory.PLAYERS, 1.0F, 1.0F);
					}
				
				}
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
			if (itemstack.getDamage() >= itemstack.getMaxDamage()) {
			return new ActionResult<>(ActionResultType.FAIL, itemstack);
		} 
		else if (EnchantmentHelper.getRiptideModifier(itemstack) > 0 && !playerIn.isWet() && (!PlayerFunctions.isHoldingWater(playerIn) && ConfigHandler.GENERAL.mustHoldBucketOfWater.get())) {
			return new ActionResult<>(ActionResultType.FAIL, itemstack);
		} 
		else {
			playerIn.setActiveHand(handIn);
			return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
		}
	}
}