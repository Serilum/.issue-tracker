/*
 * This is the latest source code of Extended Creative Inventory.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Extended Creative Inventory ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.extendedcreativeinventory;

import java.lang.reflect.Field;
import java.util.List;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.extendedcreativeinventory.config.ConfigHandler;
import com.natamus.extendedcreativeinventory.itemgroups.ExtendedItemGroup;
import com.natamus.extendedcreativeinventory.util.Reference;
import com.natamus.extendedcreativeinventory.util.Variables;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {
        instance = this;
        
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);
        
        modEventBus.register(this); // !
        MinecraftForge.EVENT_BUS.register(this);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
    
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> e) {
		if (Variables.EXTENDED == null) {
			List<String> rawconfig = ConfigFunctions.getRawConfigValues(Reference.MOD_ID);
			
			int index = 4;
			if (rawconfig.size() > 0) {
				try {
					String stringint = String.join(" ", rawconfig).replaceAll("[^0-9]", "");
					int newindex = Integer.parseInt(stringint);
					index = newindex;
				} catch (Exception ex) { }
			}
			
			Variables.EXTENDED = new ExtendedItemGroup(index, "extended");
		}
		
		if (Variables.item_group == null) {
			for (Field field : Item.class.getDeclaredFields()) {
				if (field.toString().contains("group") || field.toString().contains("category")) {
					Variables.item_group = field;
					break;
				}
			}
			if (Variables.item_group == null) {
				return;
			}
			Variables.item_group.setAccessible(true);
		}
		
		IForgeRegistry<Item> registry = e.getRegistry();
    	for (ResourceLocation rl : ForgeRegistries.ITEMS.getKeys()) {
    		Item item = ForgeRegistries.ITEMS.getValue(rl);
    		if (item.equals(Items.AIR)) {
    			continue;
    		}
    		
    		ItemGroup itemgroup = item.getItemCategory();
    		if (itemgroup == null) {
    			try {
    				Variables.item_group.set(item, Variables.EXTENDED);
    			} catch (Exception ex) { }
    			
    			registry.registerAll(item);
    		}
    	}	
	}
}
