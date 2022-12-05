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

package com.natamus.collective_fabric.functions;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.world.item.*;

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
		return itemStack.getItem() instanceof SwordItem || itemStack.is(ConventionalItemTags.SWORDS);
	}

	public static Boolean isBow(ItemStack itemStack) {
		return itemStack.getItem() instanceof BowItem || itemStack.is(ConventionalItemTags.BOWS);
	}

	public static Boolean isPickaxe(ItemStack itemStack) {
		return itemStack.getItem() instanceof PickaxeItem || itemStack.is(ConventionalItemTags.PICKAXES);
	}

	public static Boolean isAxe(ItemStack itemStack) {
		return itemStack.getItem() instanceof AxeItem || itemStack.is(ConventionalItemTags.AXES);
	}

	public static Boolean isShovel(ItemStack itemStack) {
		return itemStack.getItem() instanceof ShovelItem || itemStack.is(ConventionalItemTags.SHOVELS);
	}

	public static Boolean isHoe(ItemStack itemStack) {
		return itemStack.getItem() instanceof HoeItem || itemStack.is(ConventionalItemTags.HOES);
	}

	public static Boolean isShears(ItemStack itemStack) {
		return itemStack.getItem() instanceof ShearsItem || itemStack.is(ConventionalItemTags.SHEARS);
	}
}
