/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.erodingstoneentities.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.natamus.thevanillaexperience.mods.erodingstoneentities.config.ErodingStoneEntitiesConfigHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class ErodingStoneEntitiesUtil {
	private static List<Block> iceblocks = new ArrayList<Block>(Arrays.asList(Blocks.ICE, Blocks.BLUE_ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE));
	
	public static HashMap<Item, Item> erodeinto = new HashMap<Item, Item>();
	
	public static boolean populateArrays() {
		String tosandstring = ErodingStoneEntitiesConfigHandler.GENERAL.itemsWhichErodeIntoSand.get();
		String toredsandstring = ErodingStoneEntitiesConfigHandler.GENERAL.itemsWhichErodeIntoRedSand.get();
		String toclaystring = ErodingStoneEntitiesConfigHandler.GENERAL.itemsWhichErodeIntoClay.get();
		
		for (String itemstring : tosandstring.split(",")) {
			ResourceLocation rl = new ResourceLocation(itemstring.trim());
			if (!ForgeRegistries.ITEMS.containsKey(rl)) {
				System.out.println("[Eroding Stone Entities] Unable to find item for input '" + itemstring.trim() + "' to erode into sand in the Forge item registry. Ignoring it.");
				continue;
			}
			
			Item item = ForgeRegistries.ITEMS.getValue(rl);
			erodeinto.put(item, Items.SAND);
		}
		
		for (String itemstring : toredsandstring.split(",")) {
			ResourceLocation rl = new ResourceLocation(itemstring.trim());
			if (!ForgeRegistries.ITEMS.containsKey(rl)) {
				System.out.println("[Eroding Stone Entities] Unable to find item for input '" + itemstring.trim() + "' to erode into red sand in the Forge item registry. Ignoring it.");
				continue;
			}
			
			Item item = ForgeRegistries.ITEMS.getValue(rl);
			erodeinto.put(item, Items.RED_SAND);
		}
		
		for (String itemstring : toclaystring.split(",")) {
			ResourceLocation rl = new ResourceLocation(itemstring.trim());
			if (!ForgeRegistries.ITEMS.containsKey(rl)) {
				System.out.println("[Eroding Stone Entities] Unable to find item for input '" + itemstring.trim() + "' to erode into clay in the Forge item registry. Ignoring it.");
				continue;
			}
			
			Item item = ForgeRegistries.ITEMS.getValue(rl);
			if (ErodingStoneEntitiesConfigHandler.GENERAL.erodeIntoClayBlockInsteadOfClayBall.get()) {
				erodeinto.put(item, Items.CLAY);
			}
			else {
				erodeinto.put(item, Items.CLAY_BALL);
			}
		}
		
		if (erodeinto.size() == 0) {
			System.out.println("[Eroding Stone Entities] The erode into hashmap is empty. Something went wrong while parsing your config file. Mod disabled.");
			return false;
		}
		return true;
	}
	
	public static void transformItemEntity(Level world, ItemEntity ie) {
		ItemStack stack = ie.getItem();
		if (stack == null) {
			return;
		}
		
		Item item = stack.getItem();
		if (item == null) {
			return;
		}
		
		if (!erodeinto.containsKey(item)) {
			return;
		}
		
		Item toitem = erodeinto.get(item);
		
		ItemEntity newie = new ItemEntity(world, ie.getX(), ie.getY(), ie.getZ(), new ItemStack(toitem, stack.getCount()));
		ie.remove(RemovalReason.DISCARDED);
		world.addFreshEntity(newie);
	}
	
	public static boolean isIceBlock(Block block) {
		if (iceblocks.contains(block)) {
			return true;
		}
		return false;
	}
}
