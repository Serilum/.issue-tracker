/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.1, mod version: 2.54.
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

package com.natamus.collective.functions;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ToolType;

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
		if (itemname.contains("_sword") || itemname.contains("_pickaxe") || itemname.contains("_axe") || itemname.contains("_shovel") || itemname.contains("_hoe")) {
			return true;
		}
		return false;
	}
	
	public static Boolean isSword(ItemStack itemstack) {
		if (itemstack.getItem() instanceof SwordItem) {
			return true;
		}
		return false;
	}
	
	public static Boolean isPickaxe(ItemStack itemstack) {
		if (itemstack.getItem() instanceof PickaxeItem || itemstack.getToolTypes().contains(ToolType.PICKAXE)) {
			return true;
		}
		return false;
	}
	
	public static Boolean isAxe(ItemStack itemstack) {
		if (itemstack.getItem() instanceof AxeItem || itemstack.getToolTypes().contains(ToolType.AXE)) {
			return true;
		}
		return false;
	}
	
	public static Boolean isShovel(ItemStack itemstack) {
		if (itemstack.getItem() instanceof ShovelItem || itemstack.getToolTypes().contains(ToolType.SHOVEL)) {
			return true;
		}
		return false;
	}
	
	public static Boolean isHoe(ItemStack itemstack) {
		if (itemstack.getItem() instanceof HoeItem || itemstack.getToolTypes().contains(ToolType.HOE)) {
			return true;
		}
		return false;
	}
}
