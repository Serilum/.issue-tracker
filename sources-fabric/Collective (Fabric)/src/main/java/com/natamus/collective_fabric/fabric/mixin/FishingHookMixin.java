/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.61.
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

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;

import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;

@Mixin(value = FishingHook.class, priority = 1001)
public abstract class FishingHookMixin {
	@ModifyVariable(method = "retrieve", at = @At(value= "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/List;"))
	private List<ItemStack> FishingHook_retrieve(List<ItemStack> list, ItemStack itemStack) {
		FishingHook hook = (FishingHook)(Object)this;
		CollectiveItemEvents.ON_ITEM_FISHED.invoker().onItemFished(list, hook);
		
		return list;
	}
}
