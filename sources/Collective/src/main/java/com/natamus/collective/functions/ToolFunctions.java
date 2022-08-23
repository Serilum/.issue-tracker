/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.41.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective.functions;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ToolActions;

public class ToolFunctions {
	public static Boolean isTool(ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		}
		
		Item item = itemstack.getItem();
		if (item instanceof ShovelItem || item instanceof AxeItem || item instanceof PickaxeItem || item instanceof SwordItem || item instanceof HoeItem) {
			return true;
		}
		
		String itemname = item.toString().toLowerCase();
        return itemname.contains("_sword") || itemname.contains("_pickaxe") || itemname.contains("_axe") || itemname.contains("_shovel") || itemname.contains("_hoe");
    }
	
	public static Boolean isSword(ItemStack itemstack) {
        return itemstack.getItem() instanceof SwordItem;
    }
	
	public static Boolean isPickaxe(ItemStack itemstack) {
        return itemstack.getItem() instanceof PickaxeItem || itemstack.canPerformAction(ToolActions.PICKAXE_DIG);
    }
	
	public static Boolean isAxe(ItemStack itemstack) {
        return itemstack.getItem() instanceof AxeItem || itemstack.canPerformAction(ToolActions.AXE_DIG);
    }
	
	public static Boolean isShovel(ItemStack itemstack) {
        return itemstack.getItem() instanceof ShovelItem || itemstack.canPerformAction(ToolActions.SHOVEL_DIG);
    }
	
	public static Boolean isHoe(ItemStack itemstack) {
        return itemstack.getItem() instanceof HoeItem || itemstack.canPerformAction(ToolActions.HOE_DIG);
    }
}
