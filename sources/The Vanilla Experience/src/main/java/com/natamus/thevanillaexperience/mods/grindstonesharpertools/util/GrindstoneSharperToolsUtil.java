/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.grindstonesharpertools.util;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.grindstonesharpertools.config.GrindstoneSharperToolsConfigHandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextComponent;

public class GrindstoneSharperToolsUtil {
	public static void updateName(ItemStack itemstack, int uses) {
		if (!GrindstoneSharperToolsConfigHandler.GENERAL.showUsesLeftInItemName.get()) {
			return;
		}
		
		String prefix = GrindstoneSharperToolsConfigHandler.GENERAL.nameUsesPrefix.get();
		String name = itemstack.getHoverName().getString();
		if (name.contains(prefix)) {
			name = name.split(StringFunctions.escapeSpecialRegexChars(" " + prefix))[0];
		}
		if (uses > 0) {
			name = name + " " + GrindstoneSharperToolsConfigHandler.GENERAL.nameUsesPrefix.get() + uses + GrindstoneSharperToolsConfigHandler.GENERAL.nameUsesSuffix.get();
		}
		itemstack.setHoverName(new TextComponent(name));
	}
}
