/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.17.1, mod version: 2.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Areas ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.areas;

import com.natamus.areas.cmds.CommandAreas;
import com.natamus.areas.config.ConfigHandler;
import com.natamus.areas.events.AreaEvent;
import com.natamus.areas.events.GUIEvent;
import com.natamus.areas.network.PacketToClientShowGUI;
import com.natamus.areas.util.Reference;
import com.natamus.areas.util.Util;
import com.natamus.collective.check.RegisterMod;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {		
        instance = this;

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::loadComplete);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
		Util.network = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.MOD_ID, Reference.MOD_ID), () -> "1.0", s -> true, s -> true);
		Util.network.registerMessage(1, PacketToClientShowGUI.class, PacketToClientShowGUI::toBytes, PacketToClientShowGUI::new, PacketToClientShowGUI::handle);
    }
    
    private void loadComplete(final FMLLoadCompleteEvent event) {
		if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new GUIEvent(Minecraft.getInstance()));
		}
    	MinecraftForge.EVENT_BUS.register(new AreaEvent());
	}
    
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
    	CommandAreas.register(e.getDispatcher());
    }
}