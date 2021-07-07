/*
 * This is the latest source code of Bamboo Spreads.
 * Minecraft version: 1.16.5, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Bamboo Spreads ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.bamboospreads;

import com.natamus.bamboospreads.blocks.ExtendedBambooBlock;
import com.natamus.bamboospreads.blocks.ExtendedBambooSaplingBlock;
import com.natamus.bamboospreads.config.ConfigHandler;
import com.natamus.bamboospreads.events.PlaceEvent;
import com.natamus.bamboospreads.util.Reference;
import com.natamus.bamboospreads.variables.BlockVariables;
import com.natamus.collective.check.RegisterMod;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {
        instance = this;

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        modEventBus.addListener(this::loadComplete);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);
        
        modEventBus.register(this); // !
        MinecraftForge.EVENT_BUS.register(this);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {
    	MinecraftForge.EVENT_BUS.register(new PlaceEvent());
	}
    
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> e) {
		e.getRegistry().registerAll(
			BlockVariables.SPREADING_BAMBOO_BLOCK = new ExtendedBambooBlock(AbstractBlock.Properties.of(Material.BAMBOO, MaterialColor.PLANT).randomTicks().strength(1.0F).sound(SoundType.BAMBOO)).setRegistryName(Reference.MOD_ID, "spreadingbamboo"),
			BlockVariables.SPREADING_BAMBOO_SAPLING_BLOCK = new ExtendedBambooSaplingBlock(AbstractBlock.Properties.of(Material.BAMBOO_SAPLING).randomTicks().instabreak().noCollission().strength(1.0F).sound(SoundType.BAMBOO_SAPLING)).setRegistryName(Reference.MOD_ID, "spreadingbamboo_sapling")
		);
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(
				new BlockItem(BlockVariables.SPREADING_BAMBOO_BLOCK, new Item.Properties()).setRegistryName(Reference.MOD_ID, "spreadingbamboo"));
	}
}