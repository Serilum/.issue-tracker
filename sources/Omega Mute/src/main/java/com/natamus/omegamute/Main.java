/*
 * This is the latest source code of Omega Mute.
 * Minecraft version: 1.19.0, mod version: 2.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Omega Mute ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.omegamute;

import com.natamus.collective.check.RegisterMod;
import com.natamus.omegamute.cmds.CommandOmega;
import com.natamus.omegamute.events.MuteEvent;
import com.natamus.omegamute.util.Reference;
import com.natamus.omegamute.util.Util;
import com.natamus.omegamute.util.Variables;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {
		if (!FMLEnvironment.dist.equals(Dist.CLIENT)) {
			return;
		}
    	
        instance = this;
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
                modEventBus.addListener(this::loadComplete);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }
    
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
    	CommandOmega.register(e.getDispatcher());
    }
    
    @SubscribeEvent
	public void registerKeyBinding(RegisterKeyMappingsEvent e) {
    	Variables.hotkey = new KeyMapping("Reload Omega Mute config", 46, "key.categories.misc");
    	e.register(Variables.hotkey);    	
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {
    	try {
			Util.loadSoundFile();
		} catch (Exception ex) {
			System.out.println("Something went wrong while generating the sound file. Omega Mute has been disabled.");
			return;
		}
    	
    	MinecraftForge.EVENT_BUS.register(new MuteEvent());
	}
}
