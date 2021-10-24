/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.49.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.functions;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;

public class CompareItemFunctions {
	public static boolean isSapling(Item item) {
		if (ItemTags.SAPLINGS.contains(item) || Block.byItem(item) instanceof SaplingBlock) {
			return true;
		}
		return false;
	}
	public static boolean isSapling(ItemStack itemstack) {
		return isSapling(itemstack.getItem());
	}
	
	public static boolean isLog(Item item) {
		if (ItemTags.LOGS.contains(item)) {
			return true;
		}
		return false;
	}
	public static boolean isLog(ItemStack itemstack) {
		return isLog(itemstack.getItem());
	}
	
	public static boolean isPlank(Item item) {
		if (ItemTags.PLANKS.contains(item)) {
			return true;
		}
		return false;
	}
	public static boolean isPlank(ItemStack itemstack) {
		return isPlank(itemstack.getItem());
	}
	
	public static boolean isChest(Item item) {
		Block block = Block.byItem(item);
		if (block.equals(Blocks.CHEST) || block.equals(Blocks.TRAPPED_CHEST)) {
			return true;
		}
		return false;
	}
	public static boolean isChest(ItemStack itemstack) {
		return isChest(itemstack.getItem());
	}
	
	public static boolean isStone(Item item) {
		if (ItemTags.STONE_CRAFTING_MATERIALS.contains(item)) {
			return true;
		}
		return false;
	}
	public static boolean isStone(ItemStack itemstack) {
		return isStone(itemstack.getItem());
	}
	
	public static boolean isSlab(Item item) {
		if (ItemTags.SLABS.contains(item)) {
			return true;
		}
		return false;
	}
	public static boolean isSlab(ItemStack itemstack) {
		return isSlab(itemstack.getItem());
	}
}
