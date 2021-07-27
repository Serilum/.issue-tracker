/*
 * This is the latest source code of Placeable Blaze Rods.
 * Minecraft version: 1.17.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Placeable Blaze Rods ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.placeableblazerods;

import com.natamus.collective.check.RegisterMod;
import com.natamus.placeableblazerods.blocks.BlazeRodBlock;
import com.natamus.placeableblazerods.util.Reference;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraft.world.level.block.state.BlockBehaviour;

@Mod(Reference.MOD_ID)
public class Main {
	private static Block blazerodblock;
	public static Main instance;
	
    public Main() {
        instance = this;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::loadComplete);
        
        modEventBus.register(this); // !
        MinecraftForge.EVENT_BUS.register(this);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {

	}
    
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> e) {
		e.getRegistry().registerAll(
				blazerodblock = new BlazeRodBlock(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.0F).lightLevel((p_235454_0_) -> {
				      return 14;
				   }).sound(SoundType.WOOD).noOcclusion()).setRegistryName(Reference.MOD_ID, "blaze_rod")
		);
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(
				new BlockItem(blazerodblock, new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)).setRegistryName(Items.BLAZE_ROD.getRegistryName())
		);
	}
}