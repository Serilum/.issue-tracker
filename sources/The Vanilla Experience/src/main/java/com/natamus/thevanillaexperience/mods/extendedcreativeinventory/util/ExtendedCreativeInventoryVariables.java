/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.extendedcreativeinventory.util;

import java.lang.reflect.Field;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ExtendedCreativeInventoryVariables {
	public static Field item_group = ObfuscationReflectionHelper.findField(Item.class, "f_41377_"); // category
	public static CreativeModeTab EXTENDED = null;
}
