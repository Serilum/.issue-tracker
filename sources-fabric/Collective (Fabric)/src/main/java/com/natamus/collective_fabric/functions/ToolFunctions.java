/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.14.
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

package com.natamus.collective_fabric.functions;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;

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
		return itemstack.getItem() instanceof PickaxeItem;// || FabricToolTags.PICKAXES.contains(itemstack.getItem()); TODO: add tag support
	}

	public static Boolean isAxe(ItemStack itemstack) {
		return itemstack.getItem() instanceof AxeItem;// || FabricToolTags.AXES.contains(itemstack.getItem());
	}

	public static Boolean isShovel(ItemStack itemstack) {
		return itemstack.getItem() instanceof ShovelItem;// || FabricToolTags.SHOVELS.contains(itemstack.getItem());
	}

	public static Boolean isHoe(ItemStack itemstack) {
		return itemstack.getItem() instanceof HoeItem;// || FabricToolTags.HOES.contains(itemstack.getItem());
	}
}
