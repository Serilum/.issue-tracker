/*
 * This is the latest source code of Extended Creative Inventory.
 * Minecraft version: 1.18.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.extendedcreativeinventory;

import java.util.List;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.extendedcreativeinventory.config.ConfigHandler;
import com.natamus.extendedcreativeinventory.itemgroups.ExtendedItemGroup;
import com.natamus.extendedcreativeinventory.util.Reference;
import com.natamus.extendedcreativeinventory.util.Variables;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
		
		IForgeRegistry<Item> registry = e.getRegistry();
    	for (ResourceLocation rl : ForgeRegistries.ITEMS.getKeys()) {
    		Item item = ForgeRegistries.ITEMS.getValue(rl);
    		if (item.equals(Items.AIR)) {
    			continue;
    		}
    		
    		CreativeModeTab itemgroup = item.getItemCategory();
    		if (itemgroup == null) {
    			try {
    				Variables.item_group.set(item, Variables.EXTENDED);
    			} catch (Exception ex) { }
    			
    			registry.registerAll(item);
    		}
    	}	
	}
}
