/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.x, mod version: 4.27.
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveFurnaceEvents;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

@Mixin(value = AbstractFurnaceBlockEntity.class, priority = 1001)
public class AbstractFurnaceBlockEntityMixin {
	@Inject(method = "getBurnDuration", at = @At(value = "HEAD"), cancellable = true)
	public void AbstractFurnaceBlockEntity_getBurnDuration(ItemStack itemStack, CallbackInfoReturnable<Integer> ci) {
		if (!itemStack.isEmpty()) {
			Item item = itemStack.getItem();
			int burntime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(item, 0);
			int newburntime = CollectiveFurnaceEvents.CALCULATE_FURNACE_BURN_TIME.invoker().getFurnaceBurnTime(itemStack, burntime);
			if (burntime != newburntime) {
				ci.setReturnValue(newburntime);
			}
		}
	}
}
