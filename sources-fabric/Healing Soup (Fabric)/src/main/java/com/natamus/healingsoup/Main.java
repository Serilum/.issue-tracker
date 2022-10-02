/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.19.2, mod version: 2.9.
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

package com.natamus.healingsoup;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.healingsoup.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
import com.natamus.healingsoup.events.SoupEvent;
import com.natamus.healingsoup.items.SoupFoods;
import com.natamus.healingsoup.items.SoupItems;
import com.natamus.healingsoup.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

		registerEvents();
		registerItems();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			return SoupEvent.onPlayerInteract(player, world, hand);
		});
	}

	private void registerItems() {
		Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, "mushroom_soup"), SoupItems.MUSHROOM_SOUP);
		Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, "cactus_soup"), SoupItems.CACTUS_SOUP);
		Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, "chocolate_milk"), SoupItems.CHOCOLATE_MILK);

		SoupItems.soups.add(SoupItems.MUSHROOM_SOUP);
		SoupItems.soups.add(SoupItems.CACTUS_SOUP);
		SoupItems.soups.add(SoupItems.CHOCOLATE_MILK);
	}
}
