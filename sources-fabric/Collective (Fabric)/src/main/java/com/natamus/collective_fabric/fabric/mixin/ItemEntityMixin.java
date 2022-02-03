/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 3.20.
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(value = ItemEntity.class, priority = 1001)
public abstract class ItemEntityMixin {
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V", ordinal = 1), cancellable = true) 
	public void ItemEntity_tick(CallbackInfo ci) {
		ItemEntity itemEntity = (ItemEntity)(Object)this;
		ItemStack itemStack = itemEntity.getItem();
		CollectiveItemEvents.ON_ITEM_EXPIRE.invoker().onItemExpire(itemEntity, itemStack);
	}
}
