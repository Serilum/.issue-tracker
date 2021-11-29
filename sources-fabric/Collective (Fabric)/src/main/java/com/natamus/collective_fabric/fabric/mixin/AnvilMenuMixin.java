/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 3.8.
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveAnvilEvents;
import com.natamus.collective_fabric.objects.Triplet;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

@Mixin(value = AnvilMenu.class, priority = 1001)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
	public AnvilMenuMixin(MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
		super(menuType, i, inventory, containerLevelAccess);
	}
	
	@Shadow private String itemName;
	@Shadow private int repairItemCountCost;
	@Shadow private DataSlot cost;
	
	@Inject(method = "createResult()V", at = @At(value= "TAIL"))
	public void onCreateAnvilResult(CallbackInfo info) {
		AnvilMenu anvilmenu = (AnvilMenu)(Object)this;
		Container inputslots = this.inputSlots;
		
		ItemStack left = inputslots.getItem(0);
		ItemStack right = inputslots.getItem(1);
		ItemStack output = this.resultSlots.getItem(0);
		
		int baseCost = left.getBaseRepairCost() + (right.isEmpty() ? 0 : right.getBaseRepairCost());
		
		Triplet<Integer, Integer, ItemStack> triple = CollectiveAnvilEvents.ANVIL_CHANGE.invoker().onAnvilChange(anvilmenu, left, right, output, itemName, baseCost, this.player);
		if (triple == null) {
			return;
		}
		
		if (triple.getFirst() >= 0) {
			cost.set(triple.getFirst());
		}
		
		if (triple.getSecond() >= 0) {
			repairItemCountCost = triple.getSecond();
		}
		
		if (triple.getThird() != null) {
			this.resultSlots.setItem(0, triple.getThird());
		}
	}
}
