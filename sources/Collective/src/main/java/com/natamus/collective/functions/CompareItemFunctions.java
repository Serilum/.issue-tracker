/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.30.
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

package com.natamus.collective.functions;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;

public class CompareItemFunctions {
	public static boolean itemIsInRegistryHolder(Item item, TagKey<Item> tagKey) {
		return item.builtInRegistryHolder().is(tagKey);
	}

	public static boolean isSapling(Item item) {
		return itemIsInRegistryHolder(item, ItemTags.SAPLINGS) || Block.byItem(item) instanceof SaplingBlock;
	}
	public static boolean isSapling(ItemStack itemstack) {
		return isSapling(itemstack.getItem());
	}

	public static boolean isLog(Item item) {
		return itemIsInRegistryHolder(item, ItemTags.LOGS);
	}
	public static boolean isLog(ItemStack itemstack) {
		return isLog(itemstack.getItem());
	}

	public static boolean isPlank(Item item) {
		return itemIsInRegistryHolder(item, ItemTags.PLANKS);
	}
	public static boolean isPlank(ItemStack itemstack) {
		return isPlank(itemstack.getItem());
	}

	public static boolean isChest(Item item) {
		Block block = Block.byItem(item);
		return block.equals(Blocks.CHEST) || block.equals(Blocks.TRAPPED_CHEST);
	}
	public static boolean isChest(ItemStack itemstack) {
		return isChest(itemstack.getItem());
	}

	public static boolean isStone(Item item) {
		return itemIsInRegistryHolder(item, ItemTags.STONE_CRAFTING_MATERIALS);
	}
	public static boolean isStone(ItemStack itemstack) {
		return isStone(itemstack.getItem());
	}

	public static boolean isSlab(Item item) {
		return itemIsInRegistryHolder(item, ItemTags.SLABS);
	}
	public static boolean isSlab(ItemStack itemstack) {
		return isSlab(itemstack.getItem());
	}
}
