/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.22.
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

import org.spongepowered.asm.mixin.Final;
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
	@Final @Shadow private DataSlot cost;
	
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
