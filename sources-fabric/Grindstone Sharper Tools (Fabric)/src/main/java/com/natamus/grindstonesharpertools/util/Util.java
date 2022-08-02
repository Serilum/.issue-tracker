/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.grindstonesharpertools.util;

import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.grindstonesharpertools.config.ConfigHandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

public class Util {
	public static void updateName(ItemStack itemstack, int uses) {
		if (!ConfigHandler.showUsesLeftInItemName.getValue()) {
			return;
		}
		
		String prefix = ConfigHandler.nameUsesPrefix.getValue();
		String name = itemstack.getHoverName().getString();
		if (name.contains(prefix)) {
			name = name.split(StringFunctions.escapeSpecialRegexChars(" " + prefix))[0];
		}
		if (uses > 0) {
			name = name + " " + ConfigHandler.nameUsesPrefix.getValue() + uses + ConfigHandler.nameUsesSuffix.getValue();
		}
		itemstack.setHoverName(Component.literal(name));
	}
}
