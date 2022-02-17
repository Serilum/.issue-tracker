/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 4.8.
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

package com.natamus.collective_fabric.functions;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
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
        return itemstack.getItem() instanceof PickaxeItem || FabricToolTags.PICKAXES.contains(itemstack.getItem());
    }
	
	public static Boolean isAxe(ItemStack itemstack) {
        return itemstack.getItem() instanceof AxeItem || FabricToolTags.AXES.contains(itemstack.getItem());
    }
	
	public static Boolean isShovel(ItemStack itemstack) {
        return itemstack.getItem() instanceof ShovelItem || FabricToolTags.SHOVELS.contains(itemstack.getItem());
    }
	
	public static Boolean isHoe(ItemStack itemstack) {
        return itemstack.getItem() instanceof HoeItem || FabricToolTags.HOES.contains(itemstack.getItem());
    }
}
