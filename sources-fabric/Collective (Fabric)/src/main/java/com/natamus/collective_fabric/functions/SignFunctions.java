/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.50.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.functions;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class SignFunctions {
    public static List<String> getSignText(SignBlockEntity signentity) {
        List<String> lines = new ArrayList<String>();

        for (Component line : signentity.getMessages(false)) {
            if (line.equals(CommonComponents.EMPTY)) {
                lines.add("");
                continue;
            }

            lines.add(line.getString());
        }

        return lines;
    }
}
