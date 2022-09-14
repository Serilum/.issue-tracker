/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.2, mod version: 2.2.
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

package com.natamus.grindstonesharpertools.util;

import com.natamus.grindstonesharpertools.config.ConfigHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Util {
	public static void updateName(ItemStack itemstack, int uses) {
		if (!ConfigHandler.showUsesLeftInItemName.getValue()) {
			return;
		}

		String prefix = ConfigHandler.nameUsesPrefix.getValue();
		Component hoverName = itemstack.getHoverName();
		String name = hoverName.getString();
		List<Component> flatList = hoverName.toFlatList();

		flatList.removeIf(component -> component.toString().contains(prefix));
		if (uses > 0) {
			Style last = flatList.get(flatList.size() - 1).getStyle();
			flatList.add(
					Component.literal(" " + ConfigHandler.nameUsesPrefix.getValue()
							+ uses + ConfigHandler.nameUsesSuffix.getValue()).withStyle(last)
			);
		}

		MutableComponent mutableComponent = MutableComponent.create(ComponentContents.EMPTY);
		for (Component component : flatList) {
			mutableComponent.append(component);
		}

		itemstack.setHoverName(mutableComponent);

		if (uses == 0) {
			if ((new ItemStack(itemstack.getItem()).getHoverName().getString()).equals(itemstack.getHoverName().getString())) {
				itemstack.resetHoverName();
			}
		}
	}
}