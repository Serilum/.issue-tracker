/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.55.
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

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;

import static net.minecraft.client.Options.genericValueLabel;

public class GameSettingsFunctions {
    public static void setGammaValue() {
        Minecraft.getInstance().options.gamma = new OptionInstance("options.gamma", OptionInstance.noTooltip(), (component, double_) -> {
            int i = (int)((double)double_ * 100.0);
            if (i == 0) {
                return genericValueLabel(component, Component.translatable("options.gamma.min"));
            } else if (i == 50) {
                return genericValueLabel(component, Component.translatable("options.gamma.default"));
            } else {
                return i == 100 ? genericValueLabel(component, Component.translatable("options.gamma.max")) : genericValueLabel(component, i);
            }
        }, OptionInstance.UnitDouble.INSTANCE, 0.5, (double_) -> {
        });
    }

    public static void setGamma(Options options, double gamma) {
        options.gamma.set(gamma);
    }
    public static double getGamma(Options options) {
        return options.gamma.get();
    }
}
