/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.45.
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

package com.natamus.collective.functions;

import net.minecraft.world.item.*;
import net.minecraftforge.common.ToolActions;

public class ToolFunctions {
	public static Boolean isTool(ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}
		
		Item item = itemStack.getItem();
		if (item instanceof ShovelItem || item instanceof AxeItem || item instanceof PickaxeItem || item instanceof SwordItem || item instanceof HoeItem) {
			return true;
		}
		
		String itemname = item.toString().toLowerCase();
        return itemname.contains("_sword") || itemname.contains("_pickaxe") || itemname.contains("_axe") || itemname.contains("_shovel") || itemname.contains("_hoe");
    }
	
	public static Boolean isSword(ItemStack itemStack) {
        return itemStack.getItem() instanceof SwordItem;
    }

	public static Boolean isBow(ItemStack itemStack) {
        return itemStack.getItem() instanceof BowItem;
    }
	
	public static Boolean isPickaxe(ItemStack itemStack) {
        return itemStack.getItem() instanceof PickaxeItem || itemStack.canPerformAction(ToolActions.PICKAXE_DIG);
    }
	
	public static Boolean isAxe(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem || itemStack.canPerformAction(ToolActions.AXE_DIG);
    }
	
	public static Boolean isShovel(ItemStack itemStack) {
        return itemStack.getItem() instanceof ShovelItem || itemStack.canPerformAction(ToolActions.SHOVEL_DIG);
    }
	
	public static Boolean isHoe(ItemStack itemStack) {
        return itemStack.getItem() instanceof HoeItem || itemStack.canPerformAction(ToolActions.HOE_DIG);
    }

	public static Boolean isShears(ItemStack itemStack) {
		return itemStack.getItem() instanceof ShearsItem || itemStack.canPerformAction(ToolActions.SHEARS_DIG);
	}
}
