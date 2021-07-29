/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.17.1, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Grindstone Sharper Tools ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.grindstonesharpertools.util;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.grindstonesharpertools.config.ConfigHandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TextComponent;

public class Util {
	public static void updateName(ItemStack itemstack, int uses) {
		if (!ConfigHandler.GENERAL.showUsesLeftInItemName.get()) {
			return;
		}
		
		String prefix = ConfigHandler.GENERAL.nameUsesPrefix.get();
		String name = itemstack.getHoverName().getString();
		if (name.contains(prefix)) {
			name = name.split(StringFunctions.escapeSpecialRegexChars(" " + prefix))[0];
		}
		if (uses > 0) {
			name = name + " " + ConfigHandler.GENERAL.nameUsesPrefix.get() + uses + ConfigHandler.GENERAL.nameUsesSuffix.get();
		}
		itemstack.setHoverName(new TextComponent(name));
	}
}
