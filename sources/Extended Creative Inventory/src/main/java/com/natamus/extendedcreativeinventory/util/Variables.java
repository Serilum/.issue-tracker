/*
 * This is the latest source code of Extended Creative Inventory.
 * Minecraft version: 1.18.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.extendedcreativeinventory.util;

import java.lang.reflect.Field;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class Variables {
	public static Field item_group = ObfuscationReflectionHelper.findField(Item.class, "f_41377_"); // category
	public static CreativeModeTab EXTENDED = null;
}
