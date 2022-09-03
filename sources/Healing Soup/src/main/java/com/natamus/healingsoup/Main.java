/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.healingsoup;

import com.natamus.collective.check.RegisterMod;
import com.natamus.healingsoup.config.ConfigHandler;
import com.natamus.healingsoup.events.SoupEvent;
import com.natamus.healingsoup.items.SoupFoods;
import com.natamus.healingsoup.items.SoupItems;
import com.natamus.healingsoup.util.Reference;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;

	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
	
    public Main() {
        instance = this;

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        modEventBus.addListener(this::loadComplete);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);
        
        modEventBus.register(this); // !
        MinecraftForge.EVENT_BUS.register(this);

		registerItems();

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {
    	MinecraftForge.EVENT_BUS.register(new SoupEvent());
	}

	private void registerItems() {
		SoupItems.MUSHROOM_SOUP = new BowlFoodItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_FOOD).food(SoupFoods.MUSHROOM_SOUP));
		ITEMS.register("mushroom_soup", () -> SoupItems.MUSHROOM_SOUP);

		SoupItems.CACTUS_SOUP = new BowlFoodItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_FOOD).food(SoupFoods.MUSHROOM_SOUP));
		ITEMS.register("cactus_soup", () -> SoupItems.CACTUS_SOUP);

		SoupItems.CHOCOLATE_MILK = new BowlFoodItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_FOOD).food(SoupFoods.MUSHROOM_SOUP));
		ITEMS.register("chocolate_milk", () -> SoupItems.CHOCOLATE_MILK);

		SoupItems.soups.add(SoupItems.MUSHROOM_SOUP);
		SoupItems.soups.add(SoupItems.CACTUS_SOUP);
		SoupItems.soups.add(SoupItems.CHOCOLATE_MILK);
	}
}