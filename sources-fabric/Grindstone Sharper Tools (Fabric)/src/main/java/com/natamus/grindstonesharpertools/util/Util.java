/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
    }
}
