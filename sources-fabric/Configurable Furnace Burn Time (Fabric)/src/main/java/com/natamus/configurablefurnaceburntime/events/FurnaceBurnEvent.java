/*
 * This is the latest source code of Configurable Furnace Burn Time.
 * Minecraft version: 1.19.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.configurablefurnaceburntime.events;

import com.natamus.configurablefurnaceburntime.config.ConfigHandler;

import net.minecraft.world.item.ItemStack;

public class FurnaceBurnEvent {
	public static int furnaceBurnTimeEvent(ItemStack itemStack, int burntime) {
		return (int)Math.ceil(burntime * ConfigHandler.burnTimeModifier.getValue());
	}
}
