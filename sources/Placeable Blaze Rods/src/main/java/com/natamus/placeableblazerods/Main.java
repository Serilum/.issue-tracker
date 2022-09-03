/*
 * This is the latest source code of Placeable Blaze Rods.
 * Minecraft version: 1.18.2, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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