/*
 * This is the latest source code of Extended Creative Inventory.
 * Minecraft version: 1.18.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.extendedcreativeinventory.itemgroups;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ExtendedItemGroup extends CreativeModeTab {

	public ExtendedItemGroup(int index, String label) {
		super(index, label);
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(Items.COMMAND_BLOCK_MINECART);
	}

}
