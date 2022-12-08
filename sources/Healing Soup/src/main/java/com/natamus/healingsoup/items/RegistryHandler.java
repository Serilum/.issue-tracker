/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.19.3, mod version: 3.0.
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

package com.natamus.healingsoup.items;

import com.natamus.healingsoup.util.Reference;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class RegistryHandler {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

	public static final RegistryObject<Item> MUSHROOM_SOUP_ITEM = ITEMS.register("mushroom_soup", () -> new BowlFoodItem((new Item.Properties()).stacksTo(1).food(SoupFoods.MUSHROOM_SOUP)));
	public static final RegistryObject<Item> CACTUS_SOUP_ITEM = ITEMS.register("cactus_soup", () -> new BowlFoodItem((new Item.Properties()).stacksTo(1).food(SoupFoods.MUSHROOM_SOUP)));
	public static final RegistryObject<Item> CHOCOLATE_MILK_ITEM = ITEMS.register("chocolate_milk", () -> new BowlFoodItem((new Item.Properties()).stacksTo(1).food(SoupFoods.MUSHROOM_SOUP)));

	public static List<Item> soups = new ArrayList<Item>();

	public static Item MUSHROOM_SOUP;
	public static Item CACTUS_SOUP;
	public static Item CHOCOLATE_MILK;

    @SubscribeEvent
    public void onCreativeTab(CreativeModeTabEvent.BuildContents e) {
        e.registerSimple(CreativeModeTabs.FOOD_AND_DRINKS, MUSHROOM_SOUP);
		e.registerSimple(CreativeModeTabs.FOOD_AND_DRINKS, CACTUS_SOUP);
		e.registerSimple(CreativeModeTabs.FOOD_AND_DRINKS, CHOCOLATE_MILK);
    }
}
